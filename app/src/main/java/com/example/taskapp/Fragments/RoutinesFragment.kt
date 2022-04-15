package com.example.taskapp.Fragments

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
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.Task
import com.example.taskapp.ViewPagerAdapter

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

        private fun viewRoutineDetails(pos: Int){
        // this functions also as the way to complete tasks for now
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.expand_task_view, null)
        val taskName: TextView = dialogLayout.findViewById(R.id.taskName)
        val taskDesc: TextView =  dialogLayout.findViewById(R.id.taskDesc)
        val alertDialog = MainActivity.alertBuilder

        alertDialog.setView(dialogLayout)
        alertDialog.setTitle("")

        //Refresh task list for indexing
        val tasks:ArrayList<Task> = MainActivity.dm.getRoutines()

        taskName.text = tasks[pos].getName()
        taskDesc.text = tasks[pos].getDesc()

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

    @SuppressLint("NotifyDataSetChanged")
    //For refreshing card fragments
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

    override fun onItemClick(position: Int) {
        viewRoutineDetails(position)
    }

    override fun onClick(p0: View?) {
    }
}