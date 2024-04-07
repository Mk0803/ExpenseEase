package com.example.expenseease.ui.theme

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.system.exitProcess


@Composable
fun dismissScreen() {
    var openDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = { Text("Exit App") },
            text = { Text("Do you want to leave the app?") },
            confirmButton = {
                TextButton(onClick = { exitProcess(0) }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    BackHandler {
        openDialog = true
    }
}