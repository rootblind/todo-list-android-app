package com.example.todolist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {
    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table ORDER BY deadlineTimestamp ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE todoId = :id ORDER BY deadlineTimestamp ASC")
    fun getTasksFromTodo(id: Long): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :id")
    suspend fun getTaskById(id: Int): Task?

    @Query("SELECT * FROM task_table ORDER BY id DESC LIMIT 1")
    suspend fun getLatestTask(): Task?

}