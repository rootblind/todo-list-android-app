package com.example.todolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Todo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun destroyerDatabase(context: Context) {
            context.deleteDatabase("todo_database")
        }

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                )
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
    abstract fun todoDao(): TodoDAO
}