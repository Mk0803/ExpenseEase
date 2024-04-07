package com.example.expenseease.ui.theme

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expenseease.R
import com.example.expenseease.data.Result

@Composable
fun UserScreen(
    paddingValues: PaddingValues,
    viewModel: MainViewModel,
    navController: NavController,
    navigateLogin: () -> Unit
) {
    val userState = viewModel.userResult.observeAsState()
    val firstName = remember { mutableStateOf(TextFieldValue()) }
    val lastName = remember { mutableStateOf(TextFieldValue()) }
    var deleteAlertDialog by remember {
        mutableStateOf(false)
    }
    var signOutAlertDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }

    Column {
        when (val user = userState.value) {
            is Result.Success -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "",
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Email: ",
                            fontFamily = harmoniaSansFamily,
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Text(
                            text = user.data.email,
                            fontFamily = harmoniaSansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "First Name: ",
                            fontFamily = harmoniaSansFamily,
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Text(
                            text = user.data.firstName,
                            fontFamily = harmoniaSansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Last Name: ",
                            fontFamily = harmoniaSansFamily,
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Text(
                            text = user.data.lastName,
                            fontFamily = harmoniaSansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )


                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        modifier = Modifier
                            .height(2.dp)
                            .padding(start = 16.dp, end = 16.dp), color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .padding(16.dp)
                            .border(BorderStroke(2.dp, Color.Gray), shape = RoundedCornerShape(20))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Update User",
                                fontFamily = harmoniaSansFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )

                            OutlinedTextField(
                                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = GreenMedium,
                                    focusedLabelColor = GreenMedium,
                                    cursorColor = Color.Black
                                ),
                                value = firstName.value,
                                onValueChange = { firstName.value = it },
                                label = { Text("First Name", fontFamily = harmoniaSansFamily) }
                            )
                            OutlinedTextField(
                                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = GreenMedium,
                                    focusedLabelColor = GreenMedium,
                                    cursorColor = Color.Black
                                ),
                                value = lastName.value,
                                onValueChange = { lastName.value = it },
                                label = { Text("Last Name", fontFamily = harmoniaSansFamily) }
                            )
                            Button(
                                colors = ButtonDefaults.buttonColors(GreenDark),
                                onClick = {
                                    viewModel.updateName(firstName.value.text, lastName.value.text)
                                    viewModel.getCurrentUser()
                                    firstName.value = TextFieldValue("")
                                    lastName.value = TextFieldValue("")
                                },
                                modifier = Modifier

                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(50))
                            ) {
                                Text("Update", fontFamily = harmoniaSansFamily, color = Color.White)
                            }
                        }
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(GreenDark),
                        onClick = {
                            signOutAlertDialog = true
                        },
                        modifier = Modifier


                            .clip(RoundedCornerShape(50))
                    ) {
                        Text("Sign Out", fontFamily = harmoniaSansFamily, color = Color.White)
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(GreenDark),
                        onClick = {
                            deleteAlertDialog = true
                        },
                        modifier = Modifier

                            .padding(8.dp)
                            .clip(RoundedCornerShape(50))
                    ) {
                        Text("Delete User", fontFamily = harmoniaSansFamily, color = Color.White)

                    }


                }


                if (deleteAlertDialog) {
                    AlertDialog(
                        onDismissRequest = { deleteAlertDialog = false },
                        title = { androidx.compose.material3.Text("Delete User?") },
                        text = {
                            androidx.compose.material3.Text(
                                "Do you really want to delete all the data? \n " +
                                        "These changes cannot be reverted"
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                deleteAlertDialog = false
                                viewModel.deleteUser()
                                viewModel.signOut()
                                navigateLogin()

                            }) {
                                androidx.compose.material3.Text("Delete")

                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { deleteAlertDialog = false }) {
                                androidx.compose.material3.Text("Cancel")
                            }
                        }
                    )
                    BackHandler {
                        deleteAlertDialog = false
                    }
                }
                if (signOutAlertDialog) {
                    AlertDialog(
                        onDismissRequest = { signOutAlertDialog = false },
                        title = { androidx.compose.material3.Text("Sign Out?") },
                        text = { Text("Do you really want to sign out?") },
                        confirmButton = {
                            TextButton(onClick = {
                                signOutAlertDialog = false
                                viewModel.signOut()
                                navigateLogin()
                            }) {
                                androidx.compose.material3.Text("Sign Out")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { signOutAlertDialog = false }) {
                                androidx.compose.material3.Text("Cancel")
                            }
                        }
                    )
                    BackHandler {
                        signOutAlertDialog = false
                    }
                }
            }

            is Result.Error -> {
                // Handle error state
                Text(text = "Error: ${user.exception.message}")
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GreenDark)
                }
            }


        }
    }


}



