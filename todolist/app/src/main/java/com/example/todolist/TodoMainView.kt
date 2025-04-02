package com.example.todolist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun TodoMainView(viewModel: TodoViewModel, navController: NavController) {

    val todoList by viewModel.todoList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(32.dp)
    ) {
        if (todoList.isNotEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center,
                text = "ToDo Lists",
                fontSize = 32.sp,

            )
            LazyColumn {
                itemsIndexed(todoList) { _, item ->
                    TodoItem(
                        item = item,
                        onDelete = { viewModel.delete(item) },
                        onClick = { navController.navigate("todo_page/${item.id}") }
                        )
                }
            }
        } else {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center,
                text = "No list yet",
                fontSize = 32.sp
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .padding((16.dp))

    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate("new_list")
            },
            containerColor = Color.Green,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}

@Composable
fun TodoItem(item: Todo, onDelete: () -> Unit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = SimpleDateFormat("HH:mm, dd/MM", Locale.ENGLISH).format(item.timestamp),
                fontSize = 12.sp,
                color = Color.LightGray
            )
            Text(
                text = item.name,
                fontSize = 20.sp,
                color = Color.White
            )
            Text(
                text = item.description,
                fontSize = 12.sp,
                color = Color.White
            )
            Text(
                text = item.address,
                fontSize = 10.sp,
                color = Color.LightGray
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_forever_24),
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}
