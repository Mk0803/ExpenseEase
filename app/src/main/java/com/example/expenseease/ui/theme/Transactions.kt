package com.example.expenseease.ui.theme


import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.expenseease.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.TypeConverter
import com.example.expenseease.data.Screen
import com.example.expenseease.data.Transaction
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionScreen(navController: NavController, viewModel: MainViewModel) {
    val transactions = viewModel.transactions
    val monthlyBudget = viewModel.monthlyBudget.collectAsState()
    val showSetBudgetDialog = remember { mutableStateOf(false) }
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val gradientColors = listOf(
        Color(0xFFFF9595), // Light Coral
        Color(0xFFFF8A80), // Dark Salmon
        Color(0xFFFF7979), // Salmon
        Color(0xFFFF6D6D), // Light Salmon
        Color(0xFFFF6666)  // Medium Violet Red
    )

    val infiniteTransition = rememberInfiniteTransition()
    val animatedColors by infiniteTransition.animateColor(
        initialValue = gradientColors.first(),
        targetValue = gradientColors.last(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val filteredTransactions = remember(transactions.value, currentMonth, currentYear) {
        transactions.value.filter { transaction ->
            val transactionDate = transaction.date.split("-")
            val transactionYear = transactionDate[0].toInt()
            val transactionMonth = transactionDate[1].toInt()
            transactionMonth == currentMonth && transactionYear == currentYear
        }
    }

    var totalExpense = 0.0
    var totalIncome = 0.0

    filteredTransactions.forEach { transaction ->
        if (transaction.type == "Expense") {
            totalExpense += transaction.amount
        } else if (transaction.type == "Income") {
            totalIncome += transaction.amount
        }
    }




    if (transactions.value.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "There are no Transactions Available! \nAdd Transactions by clicking the Floating Action Button Below",
                fontFamily = harmoniaSansFamily,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    } else {
        Column {
            Card(
                elevation = 12.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(200.dp),
                shape = ShapeDefaults.ExtraLarge,

                ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    GreenDark, GreenDark
                                )
                            )
                        )
                ) {

                    Column(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .clickable { }
                                .background(
                                    brush = createGradientBrush(
                                        colors = listOf(animatedColors, gradientColors.first()),
                                        isVertical = true
                                    )
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {


                            Text(
                                text = "₹ $totalExpense",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = harmoniaSansFamily
                            )
                            Text(
                                text = "Monthly Expense", fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Pink40,
                                fontFamily = harmoniaSansFamily
                            )


                        }
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Card(
                                shape = ShapeDefaults.ExtraLarge, modifier = Modifier
                                    .weight(0.4f)
                                    .fillMaxHeight()
                                    .clickable {
                                        viewModel.getMonthlyBudget()
                                        showSetBudgetDialog.value = true }, backgroundColor = dropDownMenuGreen,
                                elevation = 20.dp
                            ) {

                                SetMonthlyBudgetDialog(
                                    viewModel = viewModel,
                                    showDialog = showSetBudgetDialog,
                                    initialBudget = monthlyBudget.value
                                )
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    viewModel.getMonthlyBudget()
                                    Text(
                                        text = "₹ ${monthlyBudget.value}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontFamily = harmoniaSansFamily
                                    )
                                    Text(
                                        text = "Monthly Budget", fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Pink40,
                                        fontFamily = harmoniaSansFamily
                                    )

                                }

                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Card(
                                shape = ShapeDefaults.ExtraLarge,
                                modifier = Modifier
                                    .weight(0.4f)
                                    .fillMaxHeight(),
                                backgroundColor = dropDownMenuGreen,
                                elevation = 20.dp
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "₹ $totalIncome",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontFamily = harmoniaSansFamily
                                    )
                                    Text(
                                        text = "Monthly Income", fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Pink40,
                                        fontFamily = harmoniaSansFamily
                                    )

                                }

                            }

                        }

                    }
                }

            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(8.dp)
            ) {
                val transactionsByDate = transactions.value.groupBy { it.date }
                transactionsByDate.entries.sortedByDescending { it.key }.forEach { (date, transactionsForDate) ->
                    stickyHeader {
                        Text(
                            text = date,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Background)
                                .padding(start = 16.dp),
                            fontFamily = harmoniaSansFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Divider(
                            modifier = Modifier
                                .height(2.dp)
                                .padding(start = 16.dp, end = 16.dp), color = Color.Gray
                        )
                    }
                    items(transactionsForDate) { transaction ->
                        TransactionLazyCard(transaction = transaction) {
                            navController.navigate("${Screen.AddEditDelete.route}/${transaction.uid}")
                        }
                    }
                }
            }

        }
    }
}


    @Composable
    fun TransactionLazyCard(
        transaction: Transaction,
        onClick: () -> Unit
    ) {
        Card(backgroundColor = cardBackground,
            elevation = 5.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick() }
                .height(70.dp),
            shape = ShapeDefaults.ExtraLarge

        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(
                                    id =
                                    if (transaction.category == "Dining")
                                        R.drawable.spoonandfork
                                    else if (transaction.category == "Groceries")
                                        R.drawable.shoppingbag
                                    else if (transaction.category == "Shopping")
                                        R.drawable.shopping
                                    else if (transaction.category == "Transit")
                                        R.drawable.metro
                                    else if (transaction.category == "Entertainment")
                                        R.drawable.tv
                                    else if (transaction.category == "Bills & Fees")
                                        R.drawable.bill
                                    else if (transaction.category == "Travel")
                                        R.drawable.travelluggage
                                    else
                                        R.drawable.money
                                ), contentDescription = ""
                            )
                        }

                    }
                    Column {
                        Text(
                            text = transaction.title,
                            fontFamily = harmoniaSansFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(30))
                                .background(GreenLight)
                                .padding(4.dp)
                        ) {

                            Text(text = transaction.category, fontFamily = harmoniaSansFamily)

                        }
                    }


                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(25.dp), painter = painterResource(
                            id =
                            if (transaction.type == "Income")
                                R.drawable.caretarrowup
                            else
                                R.drawable.down
                        ), contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    androidx.compose.material3.Text(
                        style = TextStyle(),
                        text = "\u20B9${transaction.amount}",
                        fontFamily = harmoniaSansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (transaction.type == "Income")
                            Green
                        else
                            Red
                    )
                    Spacer(modifier = Modifier.width(16.dp))


                }
            }


        }

    }@Composable
fun SetMonthlyBudgetDialog(
    viewModel: MainViewModel,
    showDialog: MutableState<Boolean>,
    initialBudget: Double
) {
    val budgetState = remember { mutableStateOf(initialBudget.toString()) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { androidx.compose.material3.Text("Set Monthly Budget") },
            text = {
                OutlinedTextField(
                    value = budgetState.value,
                    onValueChange = { budgetState.value = it },
                    label = { Text("Monthly Budget") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.setMonthlyBudget(budgetState.value.toDoubleOrNull() ?: 0.0)
                        showDialog.value = false
                    }
                ) {
                    androidx.compose.material3.Text("Set")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    androidx.compose.material3.Text("Cancel")
                }
            }
        )
    }
}



fun createGradientBrush(
    colors: List<Color>,
    isVertical: Boolean = true
): Brush {
    val endOffset = if (isVertical) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = endOffset,
        tileMode = TileMode.Clamp
    )
}
//Change 1
//Change 2
//Change 3
//Change 4

