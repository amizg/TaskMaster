package com.example.taskapp.Fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.Task
import java.util.*
import kotlin.collections.ArrayList
import javax.sql.RowSetListener

class RecyclerAdapter(cid: Int, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var cardId = cid
    private var tasks: ArrayList<Task> = MainActivity.dm.getCardTasks(cardId)


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var taskTitle: TextView
        var taskDesc: TextView
        var timeText: TextView


        init {
            taskTitle = itemView.findViewById(R.id.taskName)
            taskDesc = itemView.findViewById(R.id.taskDesc)
            timeText = itemView.findViewById(R.id.timeText)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
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
        if(tasks[position].getDeadline() > 0){
            holder.timeText.text = MainActivity.covertLongToSimpleTime(tasks[position].getDeadline())
        }
    }

    override fun getItemCount(): Int {
        var tasks = MainActivity.dm.getCardTasks(cardId)
        return tasks.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}

