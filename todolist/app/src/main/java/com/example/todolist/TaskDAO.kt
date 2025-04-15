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

    @Query("SELECT * FROM task_table WHERE deadlineTimestamp <= :now AND notified = 0")
    fun getDueUnnotifiedTasks(now: Long): Flow<List<Task>>

    @Query("UPDATE task_table SET notified = 1 WHERE id = :taskId")
    suspend fun markTaskAsNotified(taskId: Int)

    @Query("SELECT * FROM task_table ORDER BY deadlineTimestamp ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE todoId = :todoId")
    fun getTasksFromTodo(todoId: Int): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :id")
    fun getTaskById(id: Int): Flow<Task?>

    @Query("SELECT * FROM task_table ORDER BY id DESC LIMIT 1")
    suspend fun getLatestTask(): Task?

}