package com.example.taskapp.Fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.*
import com.example.taskapp.databinding.FragmentCardBinding
import java.util.*
import kotlin.collections.ArrayList
import android.content.Context
import android.os.Build
import android.view.Gravity
import androidx.annotation.RequiresApi

class CardFragment(id: Int, nm: String, taskList: ArrayList<Task>) :
    Fragment(),
    View.OnClickListener,
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener,
    RecyclerAdapter.OnItemClickListener{

    //Variables for card properties
    private var name: String = "Card"
    private var cardId: Int = 0
    private var tasks: ArrayList<Task>
    private var _binding: FragmentCardBinding? = null
    private val binding get() = _binding!!
    private lateinit var routinePage: RoutinesFragment

    //Variable for alertDialog popup
    private val alertDialog = MainActivity.alertBuilder

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

    private lateinit var dateTextView: TextView

    init{
        name = nm
        cardId = id
        tasks = taskList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        binding.cardName.text = name

        return view
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val adapter = RecyclerAdapter(MainActivity.dm.getCardTasks(cardId), this)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        //Initialize buttons
        val addTaskBtn: Button = view.findViewById(R.id.addTaskBtn)
        val menu: ImageView = view.findViewById(R.id.cardOptions)

        addTaskBtn.setOnClickListener(this)

        view.setOnClickListener(this)
        menu.setOnClickListener {
            popupCardMenu(view, requireContext())
        }

        routinePage = RoutinesFragment()
    }

    override fun onItemClick(position: Int) {
        viewTaskDetails(position)
    }

    //Called when the date is selected and the user selects "Ok"
    override fun onDateSet(view:DatePicker?, year: Int, month: Int, dayOfMonth: Int){
        selectedDay = dayOfMonth
        selectedMonth = month + 1 //This 1 needs to be here until i figure out why it is always 1 month behind
        selectedYear = year

        getDateTimeCalendar()
        TimePickerDialog(requireContext(), this, hour, minute, false).show()
    }

    //Called when the time is set and user selects "Ok"
    override fun onTimeSet(view:TimePicker?, hourOfDay: Int, minute: Int ){
        selectedHour = hourOfDay
        selectedMinute = minute

        val longDate = MainActivity.convertDateToLong("$selectedYear.$selectedMonth.$selectedDay $selectedHour:$selectedMinute")

        dateTextView.text = MainActivity.convertLongToTime(longDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // onClickListener for AddTask button
    override fun onClick(view: View) {
        when(view.id){
            R.id.addTaskBtn -> {
                addTaskBox()
            }
        }
    }

    @SuppressLint("DiscouragedPrivateApi", "RtlHardcoded")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    //three dot menu, upper right corner of card(Edit Card, Clear Completed Tasks, Delete Card)
    private fun popupCardMenu(v: View, c: Context) {
        val popupCardMenu = PopupMenu(c, v, Gravity.RIGHT, R.attr.actionOverflowMenuStyle, 0)
        val inflater = layoutInflater
        popupCardMenu.inflate(R.menu.card_menu)
        popupCardMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.editCard -> {
                    val dialogLayout = inflater.inflate(R.layout.alert_box_edittext, null)
                    val editText = dialogLayout.findViewById<EditText>(R.id.editText)

                    alertDialog.setView(dialogLayout)
                    alertDialog.setTitle("Edit Card Name")

                    alertDialog.setPositiveButton("Enter") { _, _ ->
                        MainActivity.dm.editCard(editText.text.toString(), cardId)
                        binding.cardName.text = editText.text.toString()
                    }

                    alertDialog.setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    alertDialog.setNeutralButton(""){_,_ ->}

                    alertDialog.show()
                    true
                }
                R.id.clearCompletedTasks -> {
                    val dialogLayout = inflater.inflate(R.layout.alert_box_confirmation, null)

                    alertDialog.setView(dialogLayout)
                    alertDialog.setTitle("Clear Completed Tasks?")

                    alertDialog.setPositiveButton("Yes") { _, _ ->
                        MainActivity.dm.clearCompleted(cardId)
                        refreshTasks()
                    }

                    alertDialog.setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    alertDialog.setNeutralButton(""){_,_ ->}

                    alertDialog.show()
                    true
                }
                R.id.deleteCard -> {
                    val dialogLayout = inflater.inflate(R.layout.alert_box_confirmation, null)

                    alertDialog.setView(dialogLayout)
                    alertDialog.setTitle("Delete Card?")

                    alertDialog.setPositiveButton("Yes") { _, _ ->
                        refreshCard(findCardPos(cardId, MainActivity.dm.readCards()))
                        MainActivity.dm.deleteCard(cardId)
                    }

                    alertDialog.setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    alertDialog.setNeutralButton(""){_,_ ->}

                    alertDialog.show()
                    true
                }
                else -> true
            }
        }

        popupCardMenu.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupCardMenu)
        menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(menu, true)
    }

    // opens alertDialog with detailed task information
    private fun viewTaskDetails(pos: Int){
        // this functions also as the way to complete tasks for now
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.expand_task_view, null)
        val  taskName: TextView = dialogLayout.findViewById(R.id.taskName)
        val taskDesc: TextView =  dialogLayout.findViewById(R.id.taskDesc)
        val dateChosen: TextView = dialogLayout.findViewById(R.id.selectedDateText)

        alertDialog.setView(dialogLayout)
        alertDialog.setTitle("")

        //Refresh task list for indexing
        tasks = MainActivity.dm.getCardTasks(cardId)

        taskName.text = tasks[pos].getName()
        taskDesc.text = tasks[pos].getDesc()

        //Only show deadline if it was set
        if(tasks[pos].getDeadline() > 0){
            dateChosen.text = MainActivity.convertLongToTime(tasks[pos].getDeadline())
        }

        //Mark Tasks Complete button
        alertDialog.setPositiveButton("Mark Complete") { dialog, _ ->
            MainActivity.dm.markCompleted(tasks[pos].getTaskId(), tasks[pos].getCompleted(), tasks[pos])
//            MainActivity.um.cancelTaskNotifications(tasks[pos].getTaskId())
//            MainActivity.um.clearTaskFromPreferences(tasks[pos].getTaskId())
            refreshTasks()
            dialog.dismiss()
        }

        //Edit Btn
        alertDialog.setNegativeButton("Edit"){dialog, _ ->
            //edit go here
            editTaskBox(tasks[pos])
            dialog.dismiss()
        }

        //Delete Btn
        alertDialog.setNeutralButton("Delete"){dialog, _ ->
            MainActivity.dm.deleteTask(tasks[pos].getTaskId())
//            MainActivity.um.cancelTaskNotifications(tasks[pos].getTaskId())
//            MainActivity.um.clearTaskFromPreferences(tasks[pos].getTaskId())
            refreshTasks()
            dialog.dismiss()
        }
        alertDialog.show()
    }

    // opens alertDialog to edit task selected by user
    private fun editTaskBox(task: Task){
        //For the outer alert box
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_box_edittask, null)
        val taskName = dialogLayout.findViewById<EditText>(R.id.taskName)
        val taskDesc =  dialogLayout.findViewById<EditText>(R.id.taskDesc)

        alertDialog.setTitle("Edit Task")
        alertDialog.setView(dialogLayout)

        //date pick button
        val selectDateBtn: Button = dialogLayout.findViewById(R.id.DateBtn)
        val dateChosen: TextView = dialogLayout.findViewById(R.id.selectedDateText)

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

        // establish existing data for task name, description, deadline(if applicable) and repeating days(if applicable)
        taskName.setText(task.getName())
        taskDesc.setText(task.getDesc())
        dateTextView = dateChosen
        monCB.isChecked = getDayState(task.mon)
        tueCB.isChecked = getDayState(task.tue)
        wedCB.isChecked = getDayState(task.wed)
        thuCB.isChecked = getDayState(task.thu)
        friCB.isChecked = getDayState(task.fri)
        satCB.isChecked = getDayState(task.sat)
        sunCB.isChecked = getDayState(task.sun)

        // checks if task has deadline; if true, shows deadline
        if (task.getDeadline() > 0){
            dateChosen.text = MainActivity.convertLongToTime(task.getDeadline())
        }

        //Button for selecting a deadline
        selectDateBtn.setOnClickListener{
            pickDate()
        }

        //Confirm Task button
        alertDialog.setPositiveButton("Confirm") { _, _ ->
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
            // edits task with new data
            MainActivity.dm.editTask(
                taskName.text.toString(),
                taskDesc.text.toString(),
                dateCheck(task),
                task.getCompleted(),
                task.getTaskId(),
                rp,
                mon, tue, wed, thu, fri, sat, sun,
                task.getLastCompleted()
            )

//            MainActivity.um.cancelTaskNotifications(task.getTaskId())
//            MainActivity.um.scheduleNotifications(
//                task.getDeadline(),
//                task.getName(),
//                task.getTaskId()
//            )

            //refreshes tasks
            refreshTasks()
            refreshCard(findCardPos(cardId, MainActivity.dm.readCards()))
        }
        alertDialog.setNegativeButton("Remove Deadline") { dialog, _ ->
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
                initDateCheck(),
                task.getCompleted(),
                task.getTaskId(),
                rp,
                mon, tue, wed, thu, fri, sat, sun,
                task.getLastCompleted()
            )
            refreshTasks()
            refreshCard(findCardPos(cardId, MainActivity.dm.readCards()))
            dialog.dismiss()
        }
        //Cancel
        alertDialog.setNeutralButton("Cancel"){dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    // checks if task is repeating on specified day
    private fun getDayState(day: Int):Boolean{
        return day==1
    }

    //popup for adding task
    private fun addTaskBox() {
        //Reset Dates for next task
        selectedDay = 0
        selectedMonth = 0
        selectedYear = 0
        selectedHour = 0
        selectedMinute = 0

        // repeatable
        var rp = 0

        //vars to store days of the week
        var mon = 0
        var tue = 0
        var wed = 0
        var thu = 0
        var fri = 0
        var sat = 0
        var sun = 0

        //For the outer alert box
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_box_addtask, null)
        val taskName = dialogLayout.findViewById<EditText>(R.id.taskName)
        val taskDesc =  dialogLayout.findViewById<EditText>(R.id.taskDesc)

        alertDialog.setTitle("Add New Task")
        alertDialog.setView(dialogLayout)

        //date pick button
        val selectDateBtn: Button = dialogLayout.findViewById(R.id.DateBtn)
        val dateChosen: TextView = dialogLayout.findViewById(R.id.selectedDateText)

        //checkboxes for days of week
        val monCB: CheckBox = dialogLayout.findViewById(R.id.monCB)
        val tueCB: CheckBox = dialogLayout.findViewById(R.id.tueCB)
        val wedCB: CheckBox = dialogLayout.findViewById(R.id.wedCB)
        val thuCB: CheckBox = dialogLayout.findViewById(R.id.thuCB)
        val friCB: CheckBox = dialogLayout.findViewById(R.id.friCB)
        val satCB: CheckBox = dialogLayout.findViewById(R.id.satCB)
        val sunCB: CheckBox = dialogLayout.findViewById(R.id.sunCB)

        //Set outer variable for changing if needed
        dateTextView = dateChosen

        //Button for selecting a deadline
        selectDateBtn.setOnClickListener{
            pickDate()
        }

        //Confirm Task button
        alertDialog.setPositiveButton("Add Task") { _, _ ->
            //check status of checkboxes
            if (monCB.isChecked){
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
            MainActivity.dm.addTask(
                cardId,
                taskName.text.toString(),
                taskDesc.text.toString(),
                initDateCheck(),
                0,
                rp,
                mon, tue, wed, thu, fri, sat, sun,
                0
            )
            refreshTasks()
            refreshCard(findCardPos(cardId, MainActivity.dm.readCards()))
        }
        alertDialog.setNegativeButton("") { dialog, _ ->
            dialog.dismiss()
        }
        //Cancel
        alertDialog.setNeutralButton("Cancel"){dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    //Check to see if user entered deadline
    private fun dateCheck(task: Task): Long {
        return if (selectedYear==0){
            task.getDeadline()
        } else {
            MainActivity.convertDateToLong("$selectedYear.$selectedMonth.$selectedDay $selectedHour:$selectedMinute")
        }
    }

    private fun initDateCheck(): Long {
        return if (selectedYear==0){
            0
        } else {
            MainActivity.convertDateToLong("$selectedYear.$selectedMonth.$selectedDay $selectedHour:$selectedMinute")
        }
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

        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    private fun findCardPos(cardId: Int, cardList: ArrayList<Card>): Int{

        for ((index, card) in cardList.withIndex()){

            if (cardId == card.getId()){return index + 1}
        }
        return 0
    }

    //Refresh the recycler view upon adding task
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshTasks(){
        val adapter = RecyclerAdapter(MainActivity.dm.getCardTasks(cardId), this)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        recyclerView.recycledViewPool.clear()
        adapter.notifyDataSetChanged()
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
}
