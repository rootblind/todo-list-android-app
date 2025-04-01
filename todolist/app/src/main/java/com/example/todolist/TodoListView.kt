package com.example.todolist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun TodoListView(navController: NavController, viewModel: TodoViewModel, id: Int) {

    val todo by produceState<Todo?>(initialValue = null) {
        viewModel.viewModelScope.launch {
            value = viewModel.todoDao.getTodoById(id)
        }
    }

    todo?.let {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(20.dp)
        ) {
            Text("Name: ${it.name}")
            Text("Description: ${it.description}")
            Text("Location: ${it.latitude}, ${it.longitude}")
            Text("Address: ${it.address}")
        }
    } ?: Text("Loading....")
}