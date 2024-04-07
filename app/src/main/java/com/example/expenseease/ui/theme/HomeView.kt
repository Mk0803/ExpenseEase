package com.example.expenseease.ui.theme


import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.TextField
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expenseease.R
import com.example.expenseease.data.BottomNavigationItemData
import com.example.expenseease.data.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun HomeView(viewModel: MainViewModel, navController: NavController) {
    dismissScreen()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
    ) {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(Background)
        systemUiController.setNavigationBarColor(GreenDark)

        Scaffold(
            backgroundColor = Background,
            topBar = { TopAppBar(viewModel = viewModel, onSearchQueryChanged = {}) },
            floatingActionButton = {
                if (viewModel.selectedScreen.value != "User") {
                    FloatingActionButton(
                        backgroundColor = GreenDark,
                        onClick = {
                            navController.navigate(Screen.AddEditDelete.route)
                        },
                        modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    viewModel = viewModel,

                    )
            }) { padding ->


            if (viewModel.selectedScreen.value == "User's Transactions") {
                TransactionScreen(navController = navController, viewModel)

            } else if (viewModel.selectedScreen.value == "Graphs") {

            } else {
                UserScreen(padding, viewModel = viewModel, navController = navController) {
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.HomeScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }


        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(viewModel: MainViewModel, onSearchQueryChanged: (MutableState<String>) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val sortingOptions = listOf(
        "Name",
        "Amount",
        "Type",
        "Category"
    )
    Column(
        modifier = Modifier
            .background(Background)
            .height(
                if ((viewModel.selectedScreen.value != "User's Transactions")) {
                    60.dp
                } else 120.dp
            )
            .fillMaxWidth()
    )
    {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {


                Text(
                    text = viewModel.selectedScreen.value,
                    fontFamily = harmoniaSansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )


            }
            if ((viewModel.selectedScreen.value == "User's Transactions")) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextField(
                        label = {
                            Text(
                                text = "Search",
                                fontWeight = FontWeight.Bold,
                                fontFamily = harmoniaSansFamily
                            )
                        },
                        value = viewModel.searchQuery.value,
                        onValueChange = { viewModel.searchQuery.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.8f)
                            .padding(8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Background,
                            unfocusedIndicatorColor = Background, containerColor = cardBackground
                        ),
                        shape = ShapeDefaults.ExtraLarge
                    )

                    Box {
                        Card(
                            modifier = Modifier
                                .background(Background)
                                .padding(start = 8.dp)
                                .size(width = 40.dp, height = 40.dp)
                                .clip(
                                    RoundedCornerShape(50)
                                ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .weight(0.2f)
                                    .background(Background)
                            ) {
                                Image(painter = painterResource(id = R.drawable.filter),
                                    contentDescription = "search",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable { expanded = true })
                            }

                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.wrapContentSize()
                        ) {
                            sortingOptions.forEach { option ->
                                DropdownMenuItem(onClick = {
                                    viewModel.sortingOption = when (option) {
                                        "Name" -> SortingOption.NAME
                                        "Amount" -> SortingOption.AMOUNT
                                        "Type" -> SortingOption.TYPE
                                        "Category" -> SortingOption.CATEGORY
                                        "Date" -> SortingOption.DATE
                                        else -> SortingOption.DATE
                                    }
                                    expanded = false
                                }) {
                                    Text(option)
                                }
                            }
                        }
                    }


                }

            }


        }


    }
}


val items = listOf(
    BottomNavigationItemData("User's Transactions", Icons.Rounded.Menu, "user_transactions"),
    BottomNavigationItemData("Graphs", Icons.Rounded.DateRange, "graphs_route"),
    BottomNavigationItemData("User", Icons.Rounded.AccountCircle, "user_route")
)

@Composable
fun BottomAppBar(viewModel: MainViewModel) {


    NavigationBar(
        contentColor = GreenMedium,
        modifier = Modifier
            .height(80.dp)
            .clip(RoundedCornerShape(20, 20, 0, 0))
    ) {
        Row(modifier = Modifier.background(color = GreenDark)) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(label = {
                    Text(
                        text = item.title,
                        fontFamily = harmoniaSansFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                }, selected = viewModel.selectedScreen.value == item.title, onClick = {
                    viewModel.selectedScreen.value = item.title
                }, icon = {
                    androidx.compose.material3.Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (viewModel.selectedScreen.value == item.title) Color.Black else Color.White

                    )
                })
            }
        }
    }

}



