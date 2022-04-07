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

class RecyclerAdapter(val c: Context, taskList: ArrayList<Task>, cid: Int, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var cardId = cid
    private var tasks: ArrayList<Task> = MainActivity.dm.getCardTasks(cardId)


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        View.OnClickListener {

        var taskTitle: TextView
        var taskDesc: TextView
        var menu: ImageView

        //Variables needed for deadline creation
        private var day = 0
        private var month = 0
        private var year = 0
        private var hour = 0
        private var minute = 0
        private var selectedDay = 0
        private var selectedMonth = 0
        private var selectedYear = 0
        private var selectedHour = 0
        private var selectedMinute = 0

        lateinit var dateTextView: TextView

        init {
            taskTitle = itemView.findViewById(R.id.taskName)
            taskDesc = itemView.findViewById(R.id.taskDesc)
            menu = itemView.findViewById(R.id.taskOptions)
            itemView.setOnClickListener(this)
            menu.setOnClickListener {
                popupMenu(itemView)
            }
        }


        private fun popupMenu(v: View) {
            val adapterPosition = adapterPosition
            val position = tasks[adapterPosition]
            val popupMenu = PopupMenu(c, v)
            popupMenu.inflate(R.menu.task_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.editTask -> {
                        val v = LayoutInflater.from(c).inflate(R.layout.alert_box_edittask, null, false)
                        val taskName = v.findViewById<EditText>(R.id.taskName)
                        val taskDesc = v.findViewById<EditText>(R.id.taskDesc)
                        val selectDateBtn = v.findViewById<Button>(R.id.DateBtn)
                        val taskDeadline = v.findViewById<TextView>(R.id.selectedDateText)
                        dateTextView = taskDeadline
                        selectDateBtn.setOnClickListener{
                            pickDate()
                        }
                                AlertDialog.Builder(c)
                                    .setView(v)
                                    .setPositiveButton("Confirm") {
                                        dialog,_->
                                        position.setName(taskName.text.toString())
                                        position.setDesc(taskDesc.text.toString())
                                        position.setDeadline(dateCheck())
                                        notifyDataSetChanged()
                                        MainActivity.dm.editTask(position.getName(), position.getDesc(), position.getDeadline(), position.getTaskId())
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton("Cancel") {
                                        dialog,_->
                                        dialog.dismiss()

                                    }
                                    .create()
                                    .show()
                        true
                    }
                    R.id.deleteTask -> {
                        val v = LayoutInflater.from(c).inflate(R.layout.alert_box_blank, null, false)
                        AlertDialog.Builder(c)
                            .setView(v)
                            .setTitle("Delete Task?")
                            .setPositiveButton("Yes") {
                                dialog,_->
                                MainActivity.dm.deleteTask(position.getTaskId())
                                MainActivity.dm.readTask()
                                tasks = MainActivity.dm.getTasks()
                                notifyItemRemoved(adapterPosition)
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") {
                                dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }
                    else -> true
                }

            }
            popupMenu.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenu)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }

        //Check to see if user entered deadline
        private fun dateCheck(): Long {
            return if (selectedYear == 0){
                0
            } else
                MainActivity.convertDateToLong("$selectedYear.$selectedMonth.$selectedDay $selectedHour:$selectedMinute")
        }
        //Set up Calendar for getting current d/m/y
        //and current time from system
        private fun getDateTimeCalendar(){
            val cal: Calendar = Calendar.getInstance()
            day = cal.get(Calendar.DAY_OF_MONTH)
            month = cal.get(Calendar.MONTH)
            year = cal.get(Calendar.YEAR)
            hour = cal.get(Calendar.HOUR)
            minute = cal.get(Calendar.MINUTE)
        }

        //Function for opening up and instance of the date and time selection UI
        private fun pickDate(){

            getDateTimeCalendar()

            DatePickerDialog(c, this, year, month, day).show()
        }

        //Called when the date is selected and the user selects "Ok"
        override fun onDateSet(view:DatePicker?, year: Int, month: Int, dayOfMonth: Int){
            selectedDay = dayOfMonth
            selectedMonth = month + 1 //This 1 needs to be here until i figure out why it is always 1 month behind
            selectedYear = year

            getDateTimeCalendar()
            TimePickerDialog(c, this, hour, minute, false).show()
        }
        //Called when the time is set and user selects "Ok"
        override fun onTimeSet(view:TimePicker?, hourOfDay: Int, minute: Int ){
            selectedHour = hourOfDay
            selectedMinute = minute

            //hourOfDay arg is passed as Military time by default
            //check for am/pm and format to standard time
            var ampm: String = "AM"
            if(hourOfDay >= 12){
                if (selectedHour > 12){ selectedHour -= 12}
                ampm = "PM"
            }
            //add zero for minutes under 10
            if (minute < 10){
                dateTextView.text = "$selectedMonth/$selectedDay/$selectedYear \n AT \n $selectedHour:0$selectedMinute $ampm"
            }
            else{
                dateTextView.text = "$selectedMonth/$selectedDay/$selectedYear \n AT \n $selectedHour:$selectedMinute $ampm"
            }
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
    }

    override fun getItemCount(): Int {
        var tasks = MainActivity.dm.getCardTasks(cardId)
        return tasks.size
    }



    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }





}

