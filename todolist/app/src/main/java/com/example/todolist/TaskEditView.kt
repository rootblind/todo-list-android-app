package com.example.todolist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun EditTaskView(
    navController: NavController,
    viewModel: TaskViewModel,
    taskId: Int
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val task by viewModel.getTaskById(taskId).collectAsState(initial = null)

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deadlineTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }
    
    LaunchedEffect(task) {
        task?.let {
            name = it.name
            description = it.description
            deadlineTimestamp = it.deadlineTimestamp
        }
    }

    val formattedDeadline = remember(deadlineTimestamp) {
        SimpleDateFormat("EEE, dd MMM yyyy HH:mm", Locale.getDefault()).format(deadlineTimestamp)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 32.dp, end = 16.dp)
    ) {
        Text("Edit Task", style = MaterialTheme.typography.headlineMedium)
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
                    task?.let {
                        viewModel.update(
                            it.copy(
                                name = name,
                                description = description,
                                deadlineTimestamp = deadlineTimestamp
                            )
                        )
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}
