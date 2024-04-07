package com.example.expenseease.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expenseease.data.Screen
import com.example.expenseease.data.Transaction
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val user = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(key1 = user) {
        if (user != null) {
            // User is signed in, navigate to the home screen
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.LoginScreen.route) { inclusive = true }
            }
        } else {
            // User is signed out, navigate to the login screen
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(Screen.HomeScreen.route) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeView(viewModel = viewModel, navController)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController, viewModel)
        }
        composable(Screen.SignUpScreen.route) {
            SignUpScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.AddEditDelete.route) {
            AddEditDeleteScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.AddEditDelete.route + "/{transactionId}") { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId") ?: ""

            AddEditDeleteScreen(
                navController = navController,
                viewModel = viewModel,
                transactionId
            )

        }


    }
}
