package com.ahmedzidan.finalnoteapp.data.viewmodel

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ahmedzidan.finalnoteapp.R
import com.ahmedzidan.finalnoteapp.data.models.Priority
import com.ahmedzidan.finalnoteapp.data.models.TodoData

class SharedViewModel(application: Application):AndroidViewModel(application) {
val emptyDatabase:MutableLiveData<Boolean> = MutableLiveData(false)
    fun checkIfDatabaseIsEmpty(todoData: List<TodoData>){
        emptyDatabase.value=todoData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {}
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            when(position){
                0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red)) }
                1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow)) }
                2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green)) }
            }
        }
    }




    fun checkDataFromUser(title:String,description:String):Boolean{
        return if(TextUtils.isEmpty(title)|| TextUtils.isEmpty(description)){
            false
        }else !(title.isEmpty()||description.isEmpty())
    }

    fun checkPriority(priority:String): Priority {
        return when(priority){
            "High"->{
                Priority.HIGH}
            "Medium"->{
                Priority.MEDIUM}
            "Low"->{
                Priority.LOW}
            else-> Priority.LOW
        }
    }

     fun parsePrioritiesToInt(priority: Priority):Int{
        return when(priority){
            Priority.HIGH->0
            Priority.MEDIUM->1
            Priority.LOW->2
        }
    }
    }


