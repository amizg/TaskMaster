package com.example.taskapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.viewpager2.widget.ViewPager2
import java.security.AccessController.getContext

private val TAG: String = AddCardFragment::class.java.simpleName //Debugging tag
class AddCardFragment : Fragment() {

    private val alertDialog = MainActivity.alertBuilder //for building popup screens

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater!!.inflate(R.layout.fragment_add_card, container, false)

        val addCardBtn:Button = view.findViewById(R.id.addCardBtn)

        addCardBtn.setOnClickListener{
            addCardBox()
        }

        return view
    }

    private fun addCardBox() {

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_box_edittext, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)


        alertDialog.setView(dialogLayout)
        alertDialog.setTitle("Enter Card Name")

        alertDialog.setPositiveButton("Enter") { _, _ ->
            MainActivity.dm.addCard(editText.text.toString())
            MainActivity.refresh(MainActivity.dm.getCards())
        }

        alertDialog.setNegativeButton("Cancel") { _, _ ->
        }
        alertDialog.show()
    }
}