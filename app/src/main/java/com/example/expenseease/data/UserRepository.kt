package com.example.expenseease.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Boolean> =
        try {
            if (auth.fetchSignInMethodsForEmail(email)
                    .await().signInMethods?.isNotEmpty() == true
            ) {
                Result.Error(Exception("Email is already in use"))
            } else {
                try {
                    val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                    val user = User(authResult.user?.uid.orEmpty(), firstName, lastName, email)
                    saveUserToFirestore(user)
                    createTransactionDirectory(user)
                    Result.Success(true)
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }


    suspend fun login(email: String, password: String): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun addTransaction(transaction: Transaction): Result<Boolean> {
        return try {
            val user = auth.currentUser
            if (user != null) {

                val transactionRef = firestore.collection("users").document(user.uid)
                    .collection("transactions").document(transaction.uid)
                transactionRef.set(transaction).await()

                Result.Success(true)
            } else {
                Result.Error(Exception("User not signed in"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.uid).collection("user_info")
            .document(user.uid)
            .set(
                hashMapOf(
                    "email" to user.email,
                    "firstName" to user.firstName,
                    "lastName" to user.lastName,
                    "monthlyBudget" to 0.0
                )
            )
    }


    private fun createTransactionDirectory(user: User) {
        // Create a collection named "transactions" under the user's UID
        val transactionsCollection =
            firestore.collection("users").document(user.uid).collection("transactions")
        val dummyWish = Transaction(
            uid = user.uid,
            title = "dummy wish",
            amount = 1.0,
            category = "Dining",
            date = "2024-01-01",
            time = "10:00",
            type = "Expense"
        )
        transactionsCollection.add(dummyWish)
    }

    suspend fun updateName(firstName: String, lastName: String): Result<Boolean> =
        try {
            val user = auth.currentUser
            if (user != null) {
                firestore.collection("users").document(user.uid).collection("user_info")
                    .document(user.uid)
                    .update(mapOf("firstName" to firstName, "lastName" to lastName))
                    .await()
                Result.Success(true)
            } else {
                Result.Error(Exception("User not signed in"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

    fun signOut(): Result<Boolean> =
        try {
            auth.signOut()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun deleteUser(): Result<Boolean> =
        try {
            val user = auth.currentUser
            if (user != null) {
                val batch = firestore.batch()

                // Delete the main user document
                val userDocRef = firestore.collection("users").document(user.uid)
                batch.delete(userDocRef)

                // Delete the user document in the user_info collection
                val userInfoDocRef =
                    firestore.collection("users").document(user.uid).collection("user_info")
                        .document(user.uid)
                batch.delete(userInfoDocRef)

                // Delete the user entries in the transactions collection
                val transactionsCollectionRef =
                    firestore.collection("users").document(user.uid).collection("transactions")
                val transactionsQuerySnapshot = transactionsCollectionRef.get().await()

                for (document in transactionsQuerySnapshot.documents) {
                    batch.delete(document.reference)
                }

                // Commit the batch
                batch.commit().await()

                // Delete the authentication user
                user.delete().await()

                Result.Success(true)
            } else {
                Result.Error(Exception("User not signed in"))
            }
        } catch (e: Exception) {
            e.printStackTrace() // Add this line for debugging
            Result.Error(e)
        }


    suspend fun getCurrentUser(): Result<User> =
        try {
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                val document = firestore.collection("users").document(firebaseUser.uid)
                    .collection("user_info")
                    .document(firebaseUser.uid)
                    .get()
                    .await()
                val user = document.toObject(User::class.java)
                if (user != null) {
                    Result.Success(user)
                } else {
                    Result.Error(Exception("User not found in Firestore"))
                }
            } else {
                Result.Error(Exception("User not signed in"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun updateTransaction(transaction: Transaction): Result<Boolean> =
        try {
            val user = auth.currentUser
            if (user != null) {
                val transactionRef = firestore.collection("users").document(user.uid)
                    .collection("transactions").document(transaction.uid)
                transactionRef.set(transaction)
                    .await()
                Result.Success(true)
            } else {
                Result.Error(Exception("User not signed in"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun deleteTransactionById(transactionId: String): Result<Boolean> =
        try {
            val user = auth.currentUser
            if (user != null) {
                val transactionDocRef = firestore.collection("users").document(user.uid)
                    .collection("transactions").document(transactionId)
                transactionDocRef.delete().await()
                Result.Success(true)
            } else {
                Result.Error(Exception("User not signed in"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun setMonthlyBudget(monthlyBudget: Double): Result<Boolean> =
        try {
            val user = auth.currentUser
            if (user != null) {
                firestore.collection("users").document(user.uid).collection("user_info")
                    .document(user.uid)
                    .update(mapOf("monthlyBudget" to monthlyBudget))
                    .await()
                Result.Success(true)
            } else {
                Result.Error(Exception("User not signed in"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun getMonthlyBudget(): Result<Double> =
        try {
            val user = auth.currentUser
            if (user != null) {
                val document = firestore.collection("users").document(user.uid)
                    .collection("user_info")
                    .document(user.uid)
                    .get()
                    .await()
                val monthlyBudget = document.getDouble("monthlyBudget") ?: 0.0
                Result.Success(monthlyBudget)
            } else {
                Result.Error(Exception("User not signed in"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }


}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}