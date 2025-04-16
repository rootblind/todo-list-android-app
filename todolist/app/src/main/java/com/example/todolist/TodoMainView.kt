package com.example.todolist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TodoMainView(viewModel: TodoViewModel, navController: NavController) {
    val todoList by viewModel.todoList.collectAsState()
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(32.dp)
    ) {
        val headerText = if (todoList.isNotEmpty()) "ToDo Lists" else "No list yet"

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textAlign = TextAlign.Center,
            text = headerText,
            style = typography.headlineLarge,
            color = colorScheme.onBackground
        )

        if (todoList.isNotEmpty()) {
            //Log.d("ThemeColors", "Primary color: ${colorScheme.primary}")
            //Log.d("ThemeColors", "OnBackground color: ${colorScheme.onBackground}")
            LazyColumn {
                itemsIndexed(todoList) { _, item ->
                    TodoItem(
                        item = item,
                        onDelete = { viewModel.delete(item) },
                        onClick = { navController.navigate("todo_page/${item.id}") }
                    )
                }
            }
        }
    }

    // FAB
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        FloatingActionButton(
            onClick = { navController.navigate("new_list") },
            containerColor = colorScheme.primary,
            contentColor = colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            )
        }
    }
}

@Composable
fun TodoItem(item: Todo, onDelete: () -> Unit, onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colorScheme.primary)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = SimpleDateFormat("HH:mm, dd/MM", Locale.ENGLISH).format(item.timestamp),
                style = typography.labelSmall,
                color = colorScheme.onPrimary.copy(alpha = 0.7f)
            )
            Text(
                text = item.name,
                style = typography.titleMedium,
                color = colorScheme.onPrimary
            )
            Text(
                text = item.description,
                style = typography.bodySmall,
                color = colorScheme.onPrimary
            )
            Text(
                text = item.address,
                style = typography.labelSmall,
                color = colorScheme.onPrimary.copy(alpha = 0.7f)
            )
        }
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colorScheme.error)
                .padding(8.dp)
                .clickable { onDelete() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_forever_24),
                contentDescription = "Delete",

                tint = colorScheme.onError
            )
        }
    }
}
