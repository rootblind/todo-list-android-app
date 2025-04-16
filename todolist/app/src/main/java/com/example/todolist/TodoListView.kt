package com.example.todolist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TodoListView(
    navController: NavController,
    viewModel: TodoViewModel,
    taskViewModel: TaskViewModel,
    id: Int
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val todo by produceState<Todo?>(initialValue = null) {
        viewModel.viewModelScope.launch {
            value = viewModel.todoDao.getTodoById(id)
        }
    }

    todo?.let {
        val tasks by taskViewModel.getTasksFromTodo(todo!!.id).collectAsState(initial = emptyList())
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    style = typography.headlineLarge,
                    color = colorScheme.onBackground,
                    text = it.name
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.primary)
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        style = typography.bodyLarge,
                        color = colorScheme.onPrimary,
                        text = "Description: ${it.description}"

                    )
                    Text(
                        text = "Address: ${it.address}",
                        style = typography.bodyMedium,
                        color = colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Created at ${SimpleDateFormat("HH:mm, dd/MM", Locale.ENGLISH).format(it.timestamp)}",
                        style = typography.labelSmall,
                        color = colorScheme.onPrimary.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            if(tasks.isNotEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(20.dp),
                    text = "Tasks",
                    style = typography.headlineMedium,
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                LazyColumn {
                    itemsIndexed(tasks) { _, item ->
                        TaskItem(
                            item = item,
                            onDelete = { taskViewModel.delete(item) },
                            onClick = { navController.navigate("task_page/${item.id}") },
                            onEdit = { navController.navigate("edit_task/${item.id}") }
                        )
                    }
                }
            } else {
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(20.dp),
                    text = "No tasks in this list",
                    style = typography.headlineMedium,
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }

    } ?: Text(
        text = "Loading...",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(16.dp)

    )

    Box(
        modifier = Modifier.fillMaxSize()
            .padding((16.dp))
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            FloatingActionButton(
                onClick = {
                    navController.navigate("edit_list/${todo?.id}")
                },
                containerColor = colorScheme.secondary,
                contentColor = colorScheme.onSecondary
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                )
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate("new_task/${todo?.id}")
                },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                )
            }
        }

    }
}

@Composable
fun TaskItem(
    item: Task,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {

    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colorScheme.surfaceVariant)
            //.clickable { onEdit() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Task info
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, style = typography.titleMedium)
            Text(
                text = item.description,
                style = typography.bodySmall,
                color = colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
            Text(
                text = "Deadline: ${
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(item.deadlineTimestamp)
                }",
                style = typography.labelSmall,
                color = colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Task",
            modifier = Modifier
                .padding(start = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colorScheme.secondary)
                .padding(8.dp)
                .clickable { onEdit() },
            tint = colorScheme.onSecondary
        )

        Icon(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.baseline_delete_forever_24),
            contentDescription = "Delete Task",
            modifier = Modifier
                .padding(start = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colorScheme.error)
                .padding(8.dp)
                .clickable { onDelete() },
            tint = colorScheme.onError
        )
    }
}

