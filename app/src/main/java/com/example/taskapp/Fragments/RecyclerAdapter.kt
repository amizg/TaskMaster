package com.example.taskapp.Fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.Task

class RecyclerAdapter(cid: Int) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var cardId = cid
    private val tasks: ArrayList<Task> = MainActivity.dm.getCardTasks(cardId)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var taskTitle: TextView
        var taskDesc: TextView

        init {
            taskTitle = itemView.findViewById(R.id.taskName)
            taskDesc = itemView.findViewById(R.id.taskDesc)

//            itemView.setOnClickListener {   }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_task, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.taskTitle.text = tasks[position].getName()
        holder.taskDesc.text = tasks[position].getDesc()
    }

    override fun getItemCount(): Int {
        var tasks = MainActivity.dm.getCardTasks(cardId)
        return tasks.size
    }
}