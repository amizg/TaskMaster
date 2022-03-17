package com.example.taskapp.Fragments
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.get
import androidx.fragment.app.FragmentTransaction
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentCardBinding
import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class CardFragment(id: Int, nm: String) : Fragment() {

    private var name: String = "Card"
    private var cardId: Int = 0
    private var _binding: FragmentCardBinding? = null
    private val binding get() = _binding!!
    private val alertDialog = MainActivity.alertBuilder //for building popup screens

//    private var leftName: String = "leftCard"
//    private var rightName: String = "rightCard"


    init{
        name = nm
        cardId = id

//        leftName = leftCard
//        rightName = rightCard
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        binding.cardName.text = name

//      binding.leftOfCardText.text = leftName
//      binding.rightOfCardText.text = rightName


        var editCardBtn: Button = view.findViewById(R.id.editCardBtn)
        var deleteCardBtn: Button = view.findViewById(R.id.deleteCardBtn)
        var addTaskBtn: Button = view.findViewById(R.id.addTaskBtn)

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

        alertDialog.setMessage("Delete Card?")
                //"Yes" Button
            .setPositiveButton("Yes") { _, _ ->
                // Delete selected card from database
                MainActivity.dm.deleteCard(cardId)
                MainActivity.deleteCardRefresh(MainActivity.dm.getCards())
            }
                //"No" Button
            .setNegativeButton("No") { dialog, _ ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = alertDialog.create()
        alert.show()
    }

    private fun addTaskBox(){
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_box_addtask, null)
        val  taskName = dialogLayout.findViewById<EditText>(R.id.taskName)
        val taskDesc =  dialogLayout.findViewById<EditText>(R.id.taskDesc)

        alertDialog.setView(dialogLayout)
        alertDialog.setTitle("Add New Task")

        var selectDateBtn: Button = dialogLayout.findViewById(R.id.DateBtn)
        var dateChosen: TextView = dialogLayout.findViewById(R.id.selectedDateText)

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            dateChosen.text = sdf.format(cal.time)

        }

        selectDateBtn.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        alertDialog.setPositiveButton("Add Task") { _, _ ->
            MainActivity.dm.addTask(
                cardId,
                taskName.text.toString(),
                taskDesc.text.toString(),
                1654056000000
                )
            // MainActivity.editCardRefresh(MainActivity.dm.getCards())
        }

        alertDialog.setNegativeButton("Cancel") { _, _ ->
        }
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}