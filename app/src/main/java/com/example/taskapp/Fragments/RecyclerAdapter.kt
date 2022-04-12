package com.example.taskapp.Fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
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

class RecyclerAdapter(tasks: ArrayList<Task>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

//    private var cardId = cid
//    private var tasks: ArrayList<Task> = MainActivity.dm.getCardTasks(cardId)
    var tasks = tasks


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var taskTitle: TextView = itemView.findViewById(R.id.taskName)
        var taskDesc: TextView = itemView.findViewById(R.id.taskDesc)
        var timeText: TextView = itemView.findViewById(R.id.timeText)


        init {
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
            holder.timeText.gravity = Gravity.LEFT

        }
        if(tasks[position].getCompleted() == 1){
            holder.timeText.text = "Completed"
        }
        //TODO: color tasks blue if they are due on this day of the week


        //color tasks red if they are overdue, orange if they are due in the next hour
        if(tasks[position].getDeadline() < MainActivity.currentTimeToLong()) {
            holder.timeText.setTextColor(Color.parseColor("#F44336"))
        }
        else if(tasks[position].getDeadline()-3600000 < MainActivity.currentTimeToLong()) {
            holder.timeText.setTextColor(Color.parseColor("#FFAE42"))
        }

    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}

