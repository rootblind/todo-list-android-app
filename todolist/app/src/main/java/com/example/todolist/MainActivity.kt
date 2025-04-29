package com.example.todolist

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.Manifest
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.PeriodicWorkRequestBuilder
import com.example.todolist.ui.theme.ToDoListTheme
import org.osmdroid.config.Configuration
import java.util.concurrent.TimeUnit
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy


class MainActivity : ComponentActivity() {

    companion object {
        lateinit var todoDatabase: TodoDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        todoDatabase = TodoDatabase.getDatabase(this)
        //TodoDatabase.destroyerDatabase(applicationContext)
        requestNotificationPermission()
        createNotificationChannel(applicationContext)

        // schedule a worker to check deadlines

        val request = PeriodicWorkRequestBuilder<DeadlineCheckWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "CheckTaskDeadlines",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )

        enableEdgeToEdge()

        val todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        val taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        setContent {
            val navController = rememberNavController()
            ToDoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { TodoMainView(todoViewModel, navController) }
                        composable("new_list") { NewListView(navController, todoViewModel) }
                        composable("open_map") {
                            MapScreen(navController, onLocationSelected = { location ->
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "selectedLocation",
                                    location
                                )
                                navController.popBackStack()
                            })

                        }
                        composable("todo_page/{id}") { backStackEntry ->
                            val todoId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                            if (todoId != null) {
                                TodoListView(
                                    navController,
                                    todoViewModel,
                                    taskViewModel,
                                    id = todoId
                                )
                            } else {
                                Text("Todo not found!")
                            }
                        }
                        composable("edit_list/{id}") { backStackEntry ->
                            val todoId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                            if (todoId != null) {
                                TodoListEditView(navController, todoViewModel, todoId)
                            }
                        }
                        composable("new_task/{id}") { backStackEntry ->
                            val todoId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                            if (todoId != null) {
                                NewTaskView(navController, taskViewModel, todoId = todoId)
                            }
                        }
                        composable("edit_task/{taskId}") { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
                            taskId?.let {
                                EditTaskView(navController = navController, viewModel = taskViewModel, taskId = it)
                            }
                        }


                    }
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}
