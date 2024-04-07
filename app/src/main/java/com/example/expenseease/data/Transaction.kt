package com.example.expenseease.data

data class Transaction(
    val uid: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val date: String = "",
    val time: String = "",
    val type: String = ""
)