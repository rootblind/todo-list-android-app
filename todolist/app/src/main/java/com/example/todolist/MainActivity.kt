package com.example.todolist

import android.content.Context
import android.os.Bundle
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
import com.example.todolist.ui.theme.ToDoListTheme



class MainActivity : ComponentActivity() {

    companion object {
        lateinit var todoDatabase: TodoDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoDatabase = TodoDatabase.getDatabase(applicationContext)
        enableEdgeToEdge()

        val todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        setContent{
            ToDoListTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    TodoMainView(todoViewModel)
                }
            }
        }
    }
}
