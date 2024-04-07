package com.example.expenseease.ui.theme



/*
ADDEDITDELETESCREEN
package com.example.expenseease.ui.theme

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.expenseease.data.Transaction
import java.util.Calendar
import java.util.Date
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDeleteScreen(
    navController: NavController,
    viewModel: MainViewModel,
    transaction: Transaction? = null
) {
    var title by remember { mutableStateOf(transaction?.title.orEmpty()) }
    var amount by remember { mutableStateOf(transaction?.amount?.toString().orEmpty()) }
    var category by remember { mutableStateOf(transaction?.category.orEmpty()) }
    var date by remember { mutableStateOf(transaction?.date?.let { Date(it) } ?: Date()) }
    var type by remember { mutableStateOf(transaction?.type.orEmpty()) }
    val isNewTransaction = transaction == null
    val context = LocalContext.current


    // Date Picker State
    val year: Int
    val month: Int
    val day: Int

    // Initialize the date picker state with the selected date
    val calendar = Calendar.getInstance()
    calendar.time = date
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val newDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time
            date = newDate
        },
        year,
        month,
        day
    )

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text(text = if (isNewTransaction) "Add Transaction" else "Edit Transaction") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title TextField
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") }
            )

            // Amount TextField
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions()
            )

            // Category TextField
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") }
            )


            val datePickerView = AndroidView(
                factory = { context ->
                    DatePickerView(context, datePickerDialog)
                },
                modifier = Modifier.padding(16.dp),
                update = { view ->
                    view.updateDatePickerDialog(datePickerDialog)
                }
            )

            // Date Picker UI element
            Text(
                text = date.toString(), // Display the selected date
                modifier = Modifier
                    .clickable { datePickerView.view?.showDatePicker() } // Show date picker on click
                    .padding(16.dp)
            )
            // Type TextField
            TextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Type") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save or Update Button
            Button(
                onClick = {
                    val newTransaction = Transaction(
                        uid = transaction?.uid ?: UUID.randomUUID().toString(),
                        title = title,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        category = category,
                        date = date.toString(),
                        type = type
                    )
                    if (isNewTransaction) {
                        viewModel.addTransaction(newTransaction)
                    } else {
                        viewModel.updateTransaction(newTransaction)
                    }
                    navController.popBackStack()
                }
            ) {
                Text(text = if (isNewTransaction) "Save" else "Update")
            }

            // Delete Button
            if (!isNewTransaction) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }

}
// Custom View to wrap the DatePickerDialog
class DatePickerView(context: Context, private val datePickerDialog: DatePickerDialog) : View(context) {

    private var datePickerDialogShown = false

    fun updateDatePickerDialog(dialog: DatePickerDialog) {
        // No need to show the dialog here, it will be shown on click
    }

    fun showDatePicker() {
        if (!datePickerDialogShown) {
            datePickerDialog.show()
            datePickerDialogShown = true
        }
    }
}

*/
