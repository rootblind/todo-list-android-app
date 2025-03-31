package com.example.todolist

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.theme.ToDoListTheme
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import org.osmdroid.config.Configuration


class MainActivity : ComponentActivity() {

    companion object {
        lateinit var todoDatabase: TodoDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        todoDatabase = TodoDatabase.getDatabase(applicationContext)
        enableEdgeToEdge()

        val todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        setContent{
            val navController = rememberNavController()
            ToDoListTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { TodoMainView(todoViewModel, navController)}
                        composable("new_list") {NewListView(navController)}
                        composable("open_map"){
                            MapScreen(navController, onLocationSelected = {
                                location ->
                                navController.previousBackStackEntry?.savedStateHandle?.set("selectedLocation", location)
                                navController.popBackStack()
                            })

                        }
                    }
                }
            }
        }
    }
}
