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

class CardFragment(id: Int, nm: String) : Fragment() {

    private var name: String = "Card"
    private var cardId: Int = 0
    private var _binding: FragmentCardBinding? = null
    private val binding get() = _binding!!
    private val alertDialog = MainActivity.alertBuilder //for building popup screens


    init{
        name = nm
        cardId = id
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        binding.cardName.text = name



        var editCardBtn: Button = view.findViewById(R.id.editCardBtn)
        var deleteCardBtn: Button = view.findViewById(R.id.deleteCardBtn)

        //Edit card name button
        editCardBtn.setOnClickListener{
            editCardBox()
        }
        //Delete card button
        deleteCardBtn.setOnClickListener{
            deleteCardBox()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}