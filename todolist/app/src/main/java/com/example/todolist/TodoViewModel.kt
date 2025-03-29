package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {
    private val _todoList = MutableStateFlow<List<Todo>>(emptyList())
    val todoDao = MainActivity.todoDatabase.todoDao()
    val todoList : StateFlow<List<Todo>> = _todoList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.getAllTodos().collect { todos ->
                _todoList.value = todos
            }
        }
    }

    fun insert(name : String, description: String, longitude: Double, latitude: Double){
        viewModelScope.launch(Dispatchers.IO)
        {
            todoDao.insert(
                Todo(name = name, description = description, longitude = longitude, latitude = latitude)
            )
        }
    }
    fun delete(item : Todo){
        viewModelScope.launch(Dispatchers.IO){
            todoDao.delete(item)
        }
    }
    fun update(item: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.update(item)
        }
    }

}