package com.ahmedzidan.finalnoteapp.fragment_screens.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedzidan.finalnoteapp.R
import com.ahmedzidan.finalnoteapp.data.models.Priority
import com.ahmedzidan.finalnoteapp.data.models.TodoData
import com.ahmedzidan.finalnoteapp.fragment_screens.list.util.TodoDiffUtil

class ListAdapter:RecyclerView.Adapter<ListAdapter.ViewHolder>() {
var dataList= emptyList<TodoData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTxt.text=dataList[position].title

        holder.descriptionTxt.text=dataList[position].description

       when(dataList[position].priority){
           Priority.HIGH->holder.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.red))
           Priority.MEDIUM->holder.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.green))
           Priority.LOW->holder.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.yellow))
       }
        holder.cardRow.setOnClickListener {
            val action=ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
      val titleTxt=itemView.findViewById<TextView>(R.id.titleTxt)
        val descriptionTxt=itemView.findViewById<TextView>(R.id.descriptionTxt)
        val priorityIndicator=itemView.findViewById<CardView>(R.id.priorityIndicator)
        val cardRow=itemView.findViewById<ConstraintLayout>(R.id.rowBg)

    }

//    fun setData(todoData: List<TodoData>){
//        this.dataList=todoData
//        notifyDataSetChanged()
//    }
fun setData(todoData: List<TodoData>){
    val todoDiffUtil=TodoDiffUtil(dataList,todoData)
    val result=DiffUtil.calculateDiff(todoDiffUtil)
        this.dataList=todoData
    result.dispatchUpdatesTo(this)

    }
}