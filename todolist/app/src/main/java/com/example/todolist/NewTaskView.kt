package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewTaskView(
    navController: NavController,
    viewModel: TaskViewModel,
    todoId: Int
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deadlineTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }

    val formattedDeadline = remember(deadlineTimestamp) {
        SimpleDateFormat("EEE, dd MMM yyyy HH:mm", Locale.getDefault()).format(deadlineTimestamp)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 32.dp, end = 16.dp)
    ) {
        Text("Add New Task", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Task Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Deadline: $formattedDeadline")

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            showDateTimePicker(context) { selectedTimestamp ->
                deadlineTimestamp = selectedTimestamp
            }
        }) {
            Text("Pick Date & Time")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.insert(
                        todoId = todoId,
                        name = name,
                        description = description,
                        deadlineTimestamp = deadlineTimestamp
                    )
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Task")
        }
    }
}

fun showDateTimePicker(context: Context, onTimestampSelected: (Long) -> Unit) {
    val now = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val dateCalendar = Calendar.getInstance()
            dateCalendar.set(Calendar.YEAR, year)
            dateCalendar.set(Calendar.MONTH, month)
            dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    dateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    dateCalendar.set(Calendar.MINUTE, minute)
                    dateCalendar.set(Calendar.SECOND, 0)
                    onTimestampSelected(dateCalendar.timeInMillis)
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
            ).show()
        },
        now.get(Calendar.YEAR),
        now.get(Calendar.MONTH),
        now.get(Calendar.DAY_OF_MONTH)
    ).show()
}
