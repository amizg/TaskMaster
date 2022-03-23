package com.example.taskapp.Fragments
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.usage.UsageEvents
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.Card
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.Task
import com.example.taskapp.databinding.FragmentCardBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CardFragment(id: Int, nm: String, taskList: ArrayList<Task>) : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

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

    lateinit var dateTextView: TextView

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

        //Initialize buttons
        val editCardBtn: Button = view.findViewById(R.id.editCardBtn)
        val deleteCardBtn: Button = view.findViewById(R.id.deleteCardBtn)
        val addTaskBtn: Button = view.findViewById(R.id.addTaskBtn)


        //Edit card name button
        editCardBtn.setOnClickListener{
            editCardBox()
        }
        //Delete card button
        deleteCardBtn.setOnClickListener{
            deleteCardBox()
        }
        //Add task button
        addTaskBtn.setOnClickListener {
            addTaskBox()
        }


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val adapter = RecyclerAdapter(requireContext(),tasks)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    //Pop-up edit card name screen
    private fun editCardBox(){

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_box_edittext, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)


        alertDialog.setView(dialogLayout)
        alertDialog.setTitle("Edit Card Name")

        alertDialog.setPositiveButton("Enter") { _, _ ->
            MainActivity.dm.editCard(name, cardId)
            MainActivity.editCardRefresh(MainActivity.dm.getCards())
            binding.cardName.text = editText.text.toString()
        }

        alertDialog.setNegativeButton("Cancel") { _, _ ->
        }
        alertDialog.show()

    }
    //Pop-up delete card confirmation screen
    private fun deleteCardBox(){

        alertDialog.setTitle("Delete Card?")
                //"Yes" Button
            .setPositiveButton("Yes") { _, _ ->
                // Delete selected card from database
                MainActivity.dm.deleteCard(cardId)
                MainActivity.deleteCardRefresh(findCardPos(cardId, MainActivity.dm.getCards()))

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
        val taskName = dialogLayout.findViewById<EditText>(R.id.taskName)
        val taskDesc =  dialogLayout.findViewById<EditText>(R.id.taskDesc)

        alertDialog.setView(dialogLayout)
        alertDialog.setTitle("Add New Task")

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
            //need to find out how to refresh after adding task!!!!!!!!!!!!!!!!!!
            MainActivity.addTaskRefresh(findCardPos(cardId, MainActivity.dm.getCards()))
        }
        //Cancel
        alertDialog.setNegativeButton("Cancel") { _, _ ->
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

    fun findCardPos(cardId: Int, cardList: ArrayList<Card>): Int{

        for ((index, card) in cardList.withIndex()){

            if (cardId == card.getId()){return index}
        }
        return 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}