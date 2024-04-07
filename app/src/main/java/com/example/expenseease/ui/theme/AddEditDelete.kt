package com.example.expenseease.ui.theme

import android.content.ContentValues
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expenseease.data.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.expenseease.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDeleteScreen(
    navController: NavController,
    viewModel: MainViewModel,
    transactionId: String? = null
) {

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Dining") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var time by remember { mutableStateOf(LocalTime.now().truncatedTo(ChronoUnit.MINUTES)) }
    var type by remember { mutableStateOf("Expense") }
    val isNewTransaction = transactionId == null
    var transaction by remember { mutableStateOf<Transaction?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var incomeState by remember { mutableStateOf(false) }
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(GreenDark)
    systemUiController.setNavigationBarColor(Background)

    if (transactionId != null) {
        isLoading = true
        LaunchedEffect(Unit) {
            transaction = fetchTransactionDetails(transactionId)
            title = transaction?.title ?: ""
            amount = transaction?.amount.toString()
            category = transaction?.category ?: ""
            date = LocalDate.parse(transaction?.date)
            time = LocalTime.parse(transaction?.time)
            type = transaction?.type ?: ""
            if (type == "Income")
                incomeState = true


        }
        isLoading = false
    }


    val categories = listOf(
        "Dining", "Groceries", "Shopping", "Transit", "Entertainment",
        "Bills & Fees", "Travel", "Income"
    )



    val (categoryDropdownExpanded, setCategoryDropdownExpanded) = remember { mutableStateOf(false) }



    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                modifier = Modifier.clip(RoundedCornerShape(0, 0, 20, 20)),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenDark),
                title = {
                    Text(
                        text = if (isNewTransaction) "Add Transaction" else "Edit Transaction",
                        color = Color.White,
                        fontFamily = harmoniaSansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }, backgroundColor = Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                Column {
                    TextField(
                        textStyle = TextStyle(
                            fontFamily = harmoniaSansFamily,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ),
                        value = title,
                        onValueChange = { title = it },
                        label = {
                            Text(
                                "Title",
                                fontWeight = FontWeight.Bold,
                                fontFamily = harmoniaSansFamily,
                                color = Color.White
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(50)
                            )

                            .padding(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = cardBackground,
                            unfocusedContainerColor = cardBackground,
                            disabledContainerColor = cardBackground,
                            focusedIndicatorColor = Background,
                            unfocusedIndicatorColor = Background,
                        ),
                        trailingIcon = {
                            Text(
                                text = "T",
                                fontFamily = harmoniaSansFamily,
                                color = GreenDark,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },

                        )

                    // Amount TextField
                    TextField(
                        textStyle = TextStyle(
                            fontFamily = harmoniaSansFamily,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ),
                        value = amount,
                        onValueChange = { amount = it },
                        label = {
                            Text(
                                "Amount",
                                fontWeight = FontWeight.Bold,
                                fontFamily = harmoniaSansFamily,
                                color = Color.White
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(50)
                            )

                            .padding(8.dp), colors = TextFieldDefaults.colors(
                            focusedContainerColor = cardBackground,
                            unfocusedContainerColor = cardBackground,
                            disabledContainerColor = cardBackground,
                            focusedIndicatorColor = Background,
                            unfocusedIndicatorColor = Background,
                        ),
                        trailingIcon = {
                            Text(
                                text = "₹",
                                fontFamily = harmoniaSansFamily,
                                color = GreenDark,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )


                    // Date TextField
                    TextField(
                        textStyle = TextStyle(
                            fontFamily = harmoniaSansFamily,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ),
                        value = date.toString(),
                        onValueChange = { date = LocalDate.parse(it) },
                        label = {
                            Text(
                                "Date YYYY-MM-DD",
                                fontWeight = FontWeight.Bold,
                                fontFamily = harmoniaSansFamily,
                                color = Color.White
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(50)
                            )

                            .padding(8.dp), colors = TextFieldDefaults.colors(
                            focusedContainerColor = cardBackground,
                            unfocusedContainerColor = cardBackground,
                            disabledContainerColor = cardBackground,
                            focusedIndicatorColor = Background,
                            unfocusedIndicatorColor = Background,
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = "",
                                tint = GreenDark
                            )
                        }
                    )

                    // Time TextField
                    TextField(
                        textStyle = TextStyle(
                            fontFamily = harmoniaSansFamily,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ),
                        value = time.toString(),
                        onValueChange = { time = LocalTime.parse(it) },
                        label = {
                            Text(
                                "Time  HH:MM",
                                fontWeight = FontWeight.Bold,
                                fontFamily = harmoniaSansFamily,
                                color = Color.White
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(50)
                            )

                            .padding(8.dp), colors = TextFieldDefaults.colors(
                            focusedContainerColor = cardBackground,
                            unfocusedContainerColor = cardBackground,
                            disabledContainerColor = cardBackground,
                            focusedIndicatorColor = Background,
                            unfocusedIndicatorColor = Background,
                        ),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.clock),
                                contentDescription = "",
                                tint = GreenDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )

                    Card(
                        backgroundColor = cardBackground,
                        elevation = 12.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(120.dp),
                        shape = ShapeDefaults.ExtraLarge
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    if (incomeState)
                                        Green
                                    else
                                        Red
                                )
                        ) {
                            Row(modifier = Modifier.height(50.dp)) {
                                Box(modifier = Modifier
                                    .weight(0.5f)
                                    .background(
                                        if (!incomeState)
                                            Red
                                        else
                                            LightGreen
                                    )
                                    .clickable {
                                        if (incomeState) incomeState = false
                                        type = "Expense"
                                    }) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Expense")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            painter = painterResource(
                                                id =
                                                R.drawable.down
                                            ),
                                            contentDescription = "",
                                            modifier = Modifier.size(24.dp)
                                        )

                                    }
                                }
                                Box(modifier = Modifier
                                    .weight(0.5f)
                                    .background(
                                        if (incomeState)
                                            Green
                                        else
                                            LightRed
                                    )
                                    .clickable {
                                        if (!incomeState)
                                            incomeState = true
                                        type = "Income"
                                    }) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Income")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            painter = painterResource(id = R.drawable.caretarrowup),
                                            contentDescription = "",
                                            modifier = Modifier.size(24.dp)
                                        )

                                    }
                                }


                            }
                            Box {
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxSize()
                                            .clickable { setCategoryDropdownExpanded(true) },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Image(
                                                painter = painterResource(
                                                    id =
                                                    when (category) {
                                                        "Dining" -> R.drawable.spoonandfork
                                                        "Groceries" -> R.drawable.shoppingbag
                                                        "Shopping" -> R.drawable.shopping
                                                        "Transit" -> R.drawable.metro
                                                        "Entertainment" -> R.drawable.tv
                                                        "Bills & Fees" -> R.drawable.bill
                                                        "Travel" -> R.drawable.travelluggage
                                                        else -> R.drawable.money
                                                    }
                                                ), contentDescription = ""
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = category,
                                                fontFamily = harmoniaSansFamily,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )

                                        }
                                        Row {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowDropDown,
                                                contentDescription = ""
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                        }

                                    }
                                    DropdownMenu(
                                        expanded = categoryDropdownExpanded,
                                        onDismissRequest = {
                                            setCategoryDropdownExpanded(false)
                                        }
                                    ) {
                                        categories.forEach { categoryItem ->
                                            DropdownMenuItem(onClick = {
                                                category = categoryItem
                                                setCategoryDropdownExpanded(false)

                                            }) {
                                                Row {
                                                    Spacer(modifier = Modifier.width(16.dp))
                                                    Image(
                                                        modifier = Modifier.size(24.dp),
                                                        painter = painterResource(
                                                            id =
                                                            when (categoryItem) {
                                                                "Dining" -> R.drawable.spoonandfork
                                                                "Groceries" -> R.drawable.shoppingbag
                                                                "Shopping" -> R.drawable.shopping
                                                                "Transit" -> R.drawable.metro
                                                                "Entertainment" -> R.drawable.tv
                                                                "Bills & Fees" -> R.drawable.bill
                                                                "Travel" -> R.drawable.travelluggage
                                                                else -> R.drawable.money
                                                            }
                                                        ), contentDescription = ""
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = categoryItem,
                                                        fontFamily = harmoniaSansFamily,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )

                                                }
                                            }
                                        }
                                    }
                                }


                            }


                        }

                    }
                }




                Card(
                    backgroundColor = cardBackground,
                    elevation = 12.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(60.dp),
                    shape = ShapeDefaults.ExtraLarge
                ) {
                    if (!isNewTransaction) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Box(modifier = Modifier
                                .weight(0.5f)
                                .clickable {
                                    if (title.isNotBlank() && amount.isNotBlank() && date != null && time != null) {
                                        val newTransaction = Transaction(
                                            uid = transactionId.toString(),
                                            title = title,
                                            amount = amount.toDoubleOrNull() ?: 0.0,
                                            category = category,
                                            date = date.toString(),
                                            time = time.toString(),
                                            type = type
                                        )
                                        viewModel.updateTransaction(newTransaction)
                                        navController.popBackStack()
                                    } else {
                                        Toast
                                            .makeText(
                                                context,
                                                "Please fill all required fields.",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }

                                }
                                .background(GreenDark)) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Update",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        fontFamily = harmoniaSansFamily,
                                        color = Color.White
                                    )

                                }
                            }
                            Box(modifier = Modifier
                                .weight(0.5f)
                                .clickable {
                                    viewModel.deleteTransaction(transactionId.toString())
                                    navController.popBackStack()
                                }
                                .background(Red)) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Delete",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        fontFamily = harmoniaSansFamily,
                                        color = Color.White
                                    )

                                }
                            }

                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(GreenDark)
                                .clickable {
                                    if (title.isNotBlank() && amount.isNotBlank() && date != null && time != null) {
                                        val newTransaction = Transaction(
                                            uid = UUID
                                                .randomUUID()
                                                .toString(),
                                            title = title,
                                            amount = amount.toDoubleOrNull() ?: 0.0,
                                            category = category,
                                            date = date.toString(),
                                            time = time.toString(),
                                            type = type
                                        )

                                        viewModel.addTransaction(newTransaction)
                                        navController.popBackStack()
                                    } else {
                                        // Show toast for error
                                        Toast
                                            .makeText(
                                                context,
                                                "Please fill all required fields.",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }

                                }, horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Save", fontFamily = harmoniaSansFamily,
                                fontWeight = FontWeight.Bold, fontSize = 16.sp,
                                color = Color.White
                            )
                        }

                    }

                }

            }
        }
    }
}


suspend fun fetchTransactionDetails(transactionId: String?): Transaction? {
    Log.d(ContentValues.TAG, "Fetching transaction details for ID: $transactionId")
    if (transactionId == null) {
        Log.e(ContentValues.TAG, "Transaction Id is null")
        return null
    }

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val db = FirebaseFirestore.getInstance()



    return try {
        suspendCoroutine { continuation ->
            if (user != null) {
                db.collection("users")
                    .document(user.uid)
                    .collection("transactions")
                    .document(transactionId)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val transaction = documentSnapshot.toObject(Transaction::class.java)
                            if (transaction != null) {
                                continuation.resume(transaction)
                            } else {
                                Log.e(ContentValues.TAG, "Transaction data is null")
                                continuation.resume(null)
                            }
                        } else {
                            Log.e(ContentValues.TAG, "Transaction document does not exist")
                            continuation.resume(null)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error getting Transaction document", exception)
                        continuation.resume(null)
                    }
            }
        }
    } catch (e: Exception) {
        Log.e(ContentValues.TAG, "Exception in fetchTransactionDetails", e)
        null
    }
}



