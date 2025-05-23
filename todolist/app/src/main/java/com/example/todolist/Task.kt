package com.example.todolist

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val todoId: Int,
    val name: String,
    val description: String,
    val deadlineTimestamp: Long,
    val notified: Boolean = false
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDueDateTime(): LocalDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(deadlineTimestamp), ZoneId.systemDefault())
}