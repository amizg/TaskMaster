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

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val taskList: ArrayList<Task> = MainActivity.dm.getTasks()


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_task, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.taskTitle.text = taskList[position].getName()
        holder.taskDesc.text = taskList[position].getDesc()
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var taskTitle: TextView
        var taskDesc: TextView

        init {
            taskTitle = itemView.findViewById(R.id.taskName)
            taskDesc = itemView.findViewById(R.id.taskDesc)

//            itemView.setOnClickListener {
//            }
        }
    }
}