package com.example.TaskMaster.Fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.TaskMaster.MainActivity
import com.example.TaskMaster.R
import com.example.TaskMaster.Task
import com.example.TaskMaster.ViewPagerAdapter

class RoutinesFragment :
    Fragment(),View.OnClickListener,
    RecyclerAdapter.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routines, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val adapter = RecyclerAdapter(MainActivity.dm.getRoutines(), this)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        view.setOnClickListener(this)
    }

    override fun onItemClick(position: Int) {
        viewRoutineDetails(position)
    }

    override fun onClick(p0: View?) {
    }

    private fun viewRoutineDetails(pos: Int){
        // this functions also as the way to complete tasks for now
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.expand_task_view, null)
        val taskName: TextView = dialogLayout.findViewById(R.id.taskName)
        val taskDesc: TextView =  dialogLayout.findViewById(R.id.taskDesc)
        val taskRepeatsOn: TextView = dialogLayout.findViewById((R.id.taskRepeatsOn))
        val alertDialog = MainActivity.alertBuilder
        val monText: TextView = dialogLayout.findViewById(R.id.monText)
        val tueText: TextView = dialogLayout.findViewById(R.id.tueText)
        val wedText: TextView = dialogLayout.findViewById(R.id.wedText)
        val thurText: TextView = dialogLayout.findViewById(R.id.thurText)
        val friText: TextView = dialogLayout.findViewById(R.id.friText)
        val satText: TextView = dialogLayout.findViewById(R.id.satText)
        val sunText: TextView = dialogLayout.findViewById(R.id.sunText)

        alertDialog.setView(dialogLayout)
        alertDialog.setTitle("")

        //Refresh task list for indexing
        val tasks:ArrayList<Task> = MainActivity.dm.getRoutines()

        taskName.text = tasks[pos].getName()
        taskDesc.text = tasks[pos].getDesc()
        if (tasks[pos].rp==1){
            taskRepeatsOn.text = "Repeats on:"
            if (tasks[pos].mon==1){
                monText.text="M"
            }
            if (tasks[pos].tue==1){
                tueText.text="T"
            }
            if (tasks[pos].wed==1){
                wedText.text="W"
            }
            if (tasks[pos].thu==1){
                thurText.text="T"
            }
            if (tasks[pos].fri==1){
                friText.text="F"
            }
            if (tasks[pos].sat==1){
                satText.text="S"
            }
            if (tasks[pos].sun==1){
                sunText.text="S"
            }
        }
        else{
            taskRepeatsOn.text = ""
            monText.text=""
            tueText.text=""
            wedText.text=""
            thurText.text=""
            friText.text=""
            satText.text=""
            sunText.text=""
        }

        //Mark Tasks Complete button
        alertDialog.setPositiveButton("Back") {dialog, _ ->
            dialog.dismiss()
        }

        //Edit Btn
        alertDialog.setNegativeButton("Edit"){dialog, _ ->
            //edit go here
            editRoutineBox(tasks[pos])
            dialog.dismiss()
        }

        //Delete Btn
        alertDialog.setNeutralButton("Delete"){dialog, _ ->
            MainActivity.dm.deleteTask(tasks[pos].getTaskId())
            refreshTasks()
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun editRoutineBox(task: Task){
        //For the outer alert box
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_box_editroutine, null)
        val taskName = dialogLayout.findViewById<EditText>(R.id.taskName)
        val taskDesc =  dialogLayout.findViewById<EditText>(R.id.taskDesc)
        val alertDialog = MainActivity.alertBuilder

        alertDialog.setTitle("Edit Routine")
        alertDialog.setView(dialogLayout)

        //vars to store days of the week
        var mon = 0
        var tue = 0
        var wed = 0
        var thu = 0
        var fri = 0
        var sat = 0
        var sun = 0

        // repeatable
        var rp = 0

        // checkboxes
        val monCB: CheckBox = dialogLayout.findViewById(R.id.monCB)
        val tueCB: CheckBox = dialogLayout.findViewById(R.id.tueCB)
        val wedCB: CheckBox = dialogLayout.findViewById(R.id.wedCB)
        val thuCB: CheckBox = dialogLayout.findViewById(R.id.thuCB)
        val friCB: CheckBox = dialogLayout.findViewById(R.id.friCB)
        val satCB: CheckBox = dialogLayout.findViewById(R.id.satCB)
        val sunCB: CheckBox = dialogLayout.findViewById(R.id.sunCB)

        taskName.setText(task.getName())
        taskDesc.setText(task.getDesc())

        // verify existing repeating days
        monCB.isChecked = getDayState(task.mon)
        tueCB.isChecked = getDayState(task.tue)
        wedCB.isChecked = getDayState(task.wed)
        thuCB.isChecked = getDayState(task.thu)
        friCB.isChecked = getDayState(task.fri)
        satCB.isChecked = getDayState(task.sat)
        sunCB.isChecked = getDayState(task.sun)

        //Confirm Task button
        alertDialog.setPositiveButton("Confirm") {_, _ ->
            //check status of checkboxes
            if (monCB.isChecked) {
                mon = 1
                rp = 1
            }
            if (tueCB.isChecked){
                tue = 1
                rp = 1
            }
            if (wedCB.isChecked){
                wed = 1
                rp = 1
            }
            if (thuCB.isChecked){
                thu = 1
                rp = 1
            }
            if (friCB.isChecked){
                fri = 1
                rp = 1
            }
            if (satCB.isChecked){
                sat = 1
                rp = 1
            }
            if (sunCB.isChecked){
                sun = 1
                rp = 1
            }
            MainActivity.dm.editTask(
                taskName.text.toString(),
                taskDesc.text.toString(),
                task.getDeadline(),
                task.getCompleted(),
                task.getTaskId(),
                rp,
                mon, tue, wed, thu, fri, sat, sun,
                task.getLastCompleted()
            )
            refreshTasks()
            refreshCard(MainActivity.dm.readCards().size + 3)
        }
        //Cancel
        alertDialog.setNegativeButton("") {dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setNeutralButton("Cancel"){dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun getDayState(day: Int):Boolean{
        return day==1
    }

    //For refreshing card fragments
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshCard(pos: Int){
        MainActivity.adapter = ViewPagerAdapter(MainActivity.fm, lifecycle)
        MainActivity.viewpager = MainActivity.viewpager.findViewById(R.id.viewpager)
        MainActivity.viewpager.adapter = MainActivity.adapter

        MainActivity.adapter.notifyDataSetChanged()
        MainActivity.viewpager.setCurrentItem(pos, false)
    }

    //Refresh the recycler view upon adding task
    @SuppressLint("NotifyDataSetChanged")
    fun refreshTasks(){
        val adapter = RecyclerAdapter(MainActivity.dm.getRoutines(), this)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        recyclerView.recycledViewPool.clear()
        adapter.notifyDataSetChanged()
    }
    override fun onResume() {
        super.onResume()
        refreshTasks()
    }
}