package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewModel : ViewModel() {
    private val _todoList = MutableStateFlow<List<Todo>>(emptyList())
    private val _lastId = MutableStateFlow<Int?>(null)

    val lastId: StateFlow<Int?> = _lastId.asStateFlow()
    val todoDao = MainActivity.todoDatabase.todoDao()
    val todoList : StateFlow<List<Todo>> = _todoList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.getAllTodos().collect { todos ->
                _todoList.value = todos
            }

        }
    }


    suspend fun insert(name : String,
               description: String,
               longitude: Double,
               latitude: Double,
               address: String)
    {
        val todo = Todo(name = name,
            description = description,
            longitude = longitude,
            latitude = latitude,
            address = address)
        todoDao.insert(
            todo
        )

        val newTodo = todoDao.getLatestTodo()
        newTodo?.let { _lastId.value = it.id }

    }

    fun resetLastId() {
        _lastId.value = null
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