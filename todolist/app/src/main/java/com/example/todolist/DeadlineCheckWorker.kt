package com.example.todolist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.forEach

class DeadlineCheckWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val context = applicationContext
        val viewTask = TaskViewModel()
        val now = System.currentTimeMillis()
        val dueTasks = viewTask.getDueUnnotifiedTasks(now)

        dueTasks.collect { tasks ->
            tasks.forEach{
                task ->
                showNotification(
                    context,
                    title = "Task Due: ${task.name}",
                    content = "Deadline reached: ${task.description}",
                    id = (now % 10000).toInt()
                )

                viewTask.markTaskAsNotified(task.id)
            }

        }

        return Result.success()
    }
}
