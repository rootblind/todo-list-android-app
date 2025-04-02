package com.example.todolist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TodoListView(navController: NavController, viewModel: TodoViewModel, id: Int) {

    val todo by produceState<Todo?>(initialValue = null) {
        viewModel.viewModelScope.launch {
            value = viewModel.todoDao.getTodoById(id)
        }
    }

    todo?.let {
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
                    fontSize = 32.sp,
                    color = Color.Black,
                    text = it.name
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        fontSize = 20.sp,
                        color = Color.White,
                        text = "Description: ${it.description}"

                    )
                    Text(
                        fontSize = 16.sp,
                        color = Color.LightGray,
                        text = "Address: ${it.address}"
                    )
                    Text(
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        text = "Created at ${SimpleDateFormat("HH:mm, dd/MM", Locale.ENGLISH).format(it.timestamp)}"
                    )
                }
            }
        }
    } ?: Text("Loading....")

    Box(
        modifier = Modifier.fillMaxSize()
            .padding((16.dp))

    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp), // Optional: Add padding from bottom
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Space between buttons
        ) {

            FloatingActionButton(
                onClick = {
                    navController.navigate("edit_list/${todo?.id}")
                },
                containerColor = Color.Blue,
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate("new_task")
                },
                containerColor = Color.Green,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }

    }
}