package com.example.taskapp.Fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.Task

class RecyclerAdapter(taskList: ArrayList<Task>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val tasks: ArrayList<Task> = taskList


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var taskTitle: TextView
        var taskDesc: TextView
        lateinit var deleteTaskBtn: ImageButton

        init {
            taskTitle = itemView.findViewById(R.id.taskName)
            taskDesc = itemView.findViewById(R.id.taskDesc)
            deleteTaskBtn = itemView.findViewById(R.id.deleteTaskBtn)

            deleteTaskBtn.setOnClickListener {
                MainActivity.dm.deleteTask(tasks[layoutPosition].getTaskId())
                MainActivity.addTaskRefresh(layoutPosition)
            }
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
        return tasks.size
    }
}