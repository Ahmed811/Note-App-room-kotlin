package com.ahmedzidan.finalnoteapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ahmedzidan.finalnoteapp.data.TodoDatabase
import com.ahmedzidan.finalnoteapp.data.models.TodoData
import com.ahmedzidan.finalnoteapp.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application):AndroidViewModel(application) {
    private val todoDao= TodoDatabase.getDatabase(application).todoDao()
    private val repository:TodoRepository
     val getAllData:LiveData<List<TodoData>>
     val sortByHighPriority: LiveData<List<TodoData>>
    val sortByLowPriority: LiveData<List<TodoData>>
    init {
        repository= TodoRepository(todoDao)
        getAllData=repository.getAllData
         sortByHighPriority= repository.sortByHighPriority
         sortByLowPriority= repository.sortByLowPriority
    }

    fun insertData(todoData: TodoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.inserData(todoData)
        }
    }

    fun updateData(todoData: TodoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateData(todoData)
        }
    }

    fun deleteData(todoData: TodoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteData(todoData)
        }
    }

    fun deleteAllData(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllData()
        }
    }

    fun searchDatabase(searchQuery:String):LiveData<List<TodoData>>{
        return  repository.searchDatabase(searchQuery)
    }
}