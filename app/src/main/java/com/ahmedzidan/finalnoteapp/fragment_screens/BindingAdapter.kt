package com.ahmedzidan.finalnoteapp.fragment_screens

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.ahmedzidan.finalnoteapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapter {
    companion object{
        @androidx.databinding.BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view:FloatingActionButton,navigate:Boolean){
            view.setOnClickListener {
                if(navigate){
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }
        @androidx.databinding.BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View,emptyDatabase:MutableLiveData<Boolean>){
            when(emptyDatabase.value){
                true->view.visibility=View.VISIBLE
                false->view.visibility=View.GONE
                else -> {view.visibility=View.GONE}
            }
        }

    }
}