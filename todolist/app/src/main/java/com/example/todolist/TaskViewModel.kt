package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel: ViewModel() {
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    private val _lastId = MutableStateFlow<Int?>(null)

    val lastId: StateFlow<Int?> = _lastId.asStateFlow()
    val taskDao = MainActivity.todoDatabase.taskDao()
    val taskList : StateFlow<List<Task>> = _taskList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.getAllTasks().collect { tasks ->
                _taskList.value = tasks
            }

        }
    }

    fun getTasksFromTodo(todoId: Int): Flow<List<Task>> {
        return _taskList.map {
            tasks ->
            tasks.filter {it.todoId == todoId}
        }
    }

    suspend fun insert(todoId: Int,
                       name : String,
                       description: String,
                       deadlineTimestamp: Long)
    {
        val task = Task(
            todoId = todoId,
            name = name,
            description = description,
            deadlineTimestamp = deadlineTimestamp)
        taskDao.insert(
            task
        )

        val newTask = taskDao.getLatestTask()
        newTask?.let { _lastId.value = it.id }

    }

    fun resetLastId() {
        _lastId.value = null
    }

    fun delete(item : Task){
        viewModelScope.launch(Dispatchers.IO){
            taskDao.delete(item)
        }
    }

    fun update(item: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.update(item)
        }
    }
}