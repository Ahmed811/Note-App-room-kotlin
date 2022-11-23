package com.ahmedzidan.finalnoteapp.data.repository

import androidx.lifecycle.LiveData
import com.ahmedzidan.finalnoteapp.data.TodoDao
import com.ahmedzidan.finalnoteapp.data.models.TodoData

class TodoRepository (private val todoDao: TodoDao){
    val getAllData:LiveData<List<TodoData>> =todoDao.getAllData()
    val sortByHighPriority: LiveData<List<TodoData>> = todoDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<TodoData>> =todoDao.sortByLowPriority()
    suspend fun inserData(todoData: TodoData){
        todoDao.insertData(todoData)
    }

    suspend fun updateData(todoData: TodoData){
        todoDao.updateData(todoData)
    }

    suspend fun deleteData(todoData: TodoData){
        todoDao.deleteDate(todoData)
    }

    suspend fun deleteAllData(){
        todoDao.deleteAllData()
    }

    fun searchDatabase(searchQuery:String):LiveData<List<TodoData>>{
        return todoDao.searchDatabase(searchQuery)
    }
}