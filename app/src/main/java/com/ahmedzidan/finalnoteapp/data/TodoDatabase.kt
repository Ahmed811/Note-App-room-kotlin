package com.ahmedzidan.finalnoteapp.data

import android.content.Context
import androidx.room.*
import com.ahmedzidan.finalnoteapp.data.models.TodoData

//change to true in production exportSchema = false
@Database(entities = [TodoData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class TodoDatabase:RoomDatabase() {
    abstract fun todoDao():TodoDao

    companion object{

        @Volatile
        private var INSTANCE:TodoDatabase?=null

        fun getDatabase(context: Context):TodoDatabase{
            val tempInstance= INSTANCE
            if (tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "notes_database"
                ).build()
                INSTANCE=instance
                return instance
            }
        }
    }
}