package com.example.expenseease.ui.theme

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenseease.data.Injection
import com.example.expenseease.data.Result
import com.example.expenseease.data.Transaction
import com.example.expenseease.data.User
import com.example.expenseease.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _loadingState = MutableLiveData(LoadingState.IDLE)
    val loadingState: MutableLiveData<LoadingState> = _loadingState

    enum class LoadingState { IDLE, LOADING, SUCCESS, FAILED }

    private val userRepository: UserRepository
    var selectedScreen = mutableStateOf("User's Transactions")
    var sortingOption by mutableStateOf(SortingOption.NAME)
    var searchQuery: MutableState<String> = mutableStateOf("")
    private val _authResult = MutableLiveData<com.example.expenseease.data.Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> = _authResult
    private val _userResult = MutableLiveData<Result<User>>() // Change here
    val userResult: LiveData<Result<User>> = _userResult
    val transactions = mutableStateOf(emptyList<Transaction>())


    init {
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )


        viewModelScope.launch {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            val db = FirebaseFirestore.getInstance()
            if (user != null) {
                db.collection("users").document(user.uid).collection("transactions")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.w("Transactions", "Error fetching transactions: $error")
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val newTransaction = snapshot.mapNotNull { document ->
                                try {
                                    Transaction(
                                        uid = document.id, // Add ID if needed
                                        title = document["title"] as? String ?: "",
                                        amount = (document["amount"] as? Double ?: 0.0),
                                        category = document["category"] as? String ?: "",
                                        date = document["date"] as? String ?: "",
                                        type = document["type"] as? String ?: "",
                                        time = document["time"] as? String ?: ""
                                    )
                                } catch (e: Exception) {
                                    Log.e(ContentValues.TAG, "Error converting document to Transaction", e)
                                    null
                                }
                            }
                            transactions.value = newTransaction
                        }
                    }
            }
        }
    }


    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LOADING
            _authResult.value = userRepository.signUp(email, password, firstName, lastName)
            _loadingState.value = when (_authResult.value) {
                is Result.Success -> LoadingState.SUCCESS
                is Result.Error -> LoadingState.FAILED
                else -> LoadingState.IDLE
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.login(email, password)
        }
    }

    fun updateName(firstName: String, lastName: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.updateName(firstName, lastName)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _authResult.value = userRepository.signOut()
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            _authResult.value = userRepository.deleteUser()
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _userResult.value = userRepository.getCurrentUser()
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LOADING
            val result = userRepository.addTransaction(transaction)
            _loadingState.value = when (result) {
                is Result.Success -> LoadingState.SUCCESS
                is Result.Error -> LoadingState.FAILED
                else -> LoadingState.IDLE
            }
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LOADING
            _authResult.value = userRepository.updateTransaction(transaction)
            _loadingState.value = when (_authResult.value) {
                is Result.Success -> LoadingState.SUCCESS
                is Result.Error -> LoadingState.FAILED
                else -> LoadingState.IDLE
            }
        }
    }

    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LOADING
            val result = userRepository.deleteTransactionById(transactionId)
            _loadingState.value = when (result) {
                is Result.Success -> LoadingState.SUCCESS
                is Result.Error -> LoadingState.FAILED
                else -> LoadingState.IDLE
            }
        }
    }

    private val _monthlyBudget = MutableStateFlow(0.0)
    val monthlyBudget: StateFlow<Double> = _monthlyBudget
    fun setMonthlyBudget(monthlyBudget: Double) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LOADING
            _authResult.value = userRepository.setMonthlyBudget(monthlyBudget)
            _loadingState.value = when (_authResult.value) {
                is Result.Success -> {
                    _monthlyBudget.value = monthlyBudget
                    LoadingState.SUCCESS
                }
                is Result.Error -> LoadingState.FAILED
                else -> LoadingState.IDLE
            }
        }
    }

    fun getMonthlyBudget() {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LOADING
            val result = userRepository.getMonthlyBudget()
            _monthlyBudget.value = when (result) {
                is Result.Success -> result.data
                is Result.Error -> 0.0
                else -> { 0.0}
            }
            _loadingState.value = LoadingState.SUCCESS
        }
    }


}

enum class SortingOption {
    NAME,
    AMOUNT,
    TYPE,
    CATEGORY,
    DATE
}
