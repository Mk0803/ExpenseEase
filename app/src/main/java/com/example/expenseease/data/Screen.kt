package com.example.expenseease.data

sealed class Screen(val route:String){
    object LoginScreen: Screen("Login")
    object SignUpScreen: Screen("SignUp")
    object AddEditDelete: Screen("AddEditDelete")
    object HomeScreen: Screen("HomeScreen")
}