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

class CardFragment(id: Int, nm: String, taskList: ArrayList<Task>) :
    Fragment(),
    View.OnClickListener,
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener,
    RecyclerAdapter.OnItemClickListener{

    private var name: String = "Card"
    private var cardId: Int = 0
    private var tasks: ArrayList<Task>
    private var _binding: FragmentCardBinding? = null
    private val binding get() = _binding!!
    private val alertDialog = MainActivity.alertBuilder //for building popup screens

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val adapter = RecyclerAdapter(cardId, this)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        //Initialize buttons
        val editCardBtn: Button = view.findViewById(R.id.editCardBtn)
        val deleteCardBtn: Button = view.findViewById(R.id.deleteCardBtn)
        val addTaskBtn: Button = view.findViewById(R.id.addTaskBtn)
        editCardBtn.setOnClickListener(this)
        deleteCardBtn.setOnClickListener(this)
        addTaskBtn.setOnClickListener(this)
    }
    override fun onItemClick(position: Int) {
        viewTaskDetails(position)
    }

    private fun viewTaskDetails(pos: Int){

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
        //date pick button
        //val selectDateBtn: Button = dialogLayout.findViewById(R.id.DateBtn)

        //Button for selecting a deadline
//        selectDateBtn.setOnClickListener{
//            pickDate()
//        }

        //Confirm button
        alertDialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.setNegativeButton("Cancel/Edit/Delete?"){dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    //Refresh the recycler view upon adding task
    @SuppressLint("NotifyDataSetChanged")
    private fun addTaskRefresh(){
        val adapter = RecyclerAdapter(cardId, this)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        recyclerView.recycledViewPool.clear()
        adapter.notifyDataSetChanged()
    }

    //Pop-up edit card name screen
    private fun editCardBox(){

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_box_edittext, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)
        alertDialog.setView(dialogLayout)

        alertDialog.setTitle("Edit Card Name")

        alertDialog.setPositiveButton("Enter") { _, _ ->
            MainActivity.dm.editCard(editText.text.toString(), cardId)
            binding.cardName.text = editText.text.toString()
        }

        alertDialog.setNegativeButton("Cancel") { _, _ ->
        }
        alertDialog.show()
    }
    //Pop-up delete card confirmation screen
    private fun deleteCardBox(){
        //Blank layout for alert dialog
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_box_confirmation, null)
        alertDialog.setView(dialogLayout)

        alertDialog.setTitle("Delete Card?")
                //"Yes" Button
            .setPositiveButton("Yes") { _, _ ->
                // Delete selected card from database
                refreshCard(findCardPos(cardId, MainActivity.dm.getCards()))
                MainActivity.dm.deleteCard(cardId)
            }
                //"No" Button
            .setNegativeButton("No") { dialog, _ ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = alertDialog.create()
        alert.show()
    }
    //popup for adding task
    private fun addTaskBox() {
        //For the outer alert box
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_box_addtask, null)
        val  taskName = dialogLayout.findViewById<EditText>(R.id.taskName)
        val taskDesc =  dialogLayout.findViewById<EditText>(R.id.taskDesc)

        alertDialog.setTitle("Add New Task")
        alertDialog.setView(dialogLayout)


        //date pick button
        val selectDateBtn: Button = dialogLayout.findViewById(R.id.DateBtn)
        val dateChosen: TextView = dialogLayout.findViewById(R.id.selectedDateText)

        //Set outer variable for changing if needed
        dateTextView = dateChosen

        //Button for selecting a deadline
        selectDateBtn.setOnClickListener{
            pickDate()
        }

        //Confirm Task button
        alertDialog.setPositiveButton("Add Task") { _, _ ->
            MainActivity.dm.addTask(
                cardId,
                taskName.text.toString(),
                taskDesc.text.toString(),
                dateCheck()
                )
            addTaskRefresh()
        }
        //Cancel
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
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

        DatePickerDialog(requireContext(), this, year, month, day).show()
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

        var longDate = MainActivity.convertDateToLong("$selectedYear.$selectedMonth.$selectedDay $selectedHour:$selectedMinute")

        dateTextView.text = MainActivity.convertLongToTime(longDate)
    }

    private fun findCardPos(cardId: Int, cardList: ArrayList<Card>): Int{

        for ((index, card) in cardList.withIndex()){

            if (cardId == card.getId()){return index}
        }
        return 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View) {
       when(view.id){
           R.id.editCardBtn -> {
               editCardBox()
           }
           R.id.deleteCardBtn -> {
               deleteCardBox()
           }
           R.id.addTaskBtn -> {
               addTaskBox()
           }
       }
    }
    //For refreshing card fragments
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshCard(pos: Int){
        MainActivity.adapter = ViewPagerAdapter(MainActivity.fm, lifecycle)
        MainActivity.viewpager = MainActivity.viewpager.findViewById(R.id.viewpager)
        MainActivity.viewpager.adapter = MainActivity.adapter

        MainActivity.adapter.notifyDataSetChanged()
        MainActivity.viewpager.setCurrentItem(pos, true)
    }

}