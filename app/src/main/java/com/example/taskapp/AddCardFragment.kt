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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater!!.inflate(R.layout.fragment_add_card, container, false)

        var confirmAddCardBtn:Button = view.findViewById(R.id.confirmAddCard)
        var addCardEditText:EditText = view.findViewById(R.id.newCardNameField)

        confirmAddCardBtn.setOnClickListener{
            if (addCardEditText.text.toString() != "")
            {
                MainActivity.dm.addCard(addCardEditText.text.toString())
                MainActivity.refresh(MainActivity.dm.getCards())
            }
        }

        // Inflate the layout for this fragment
        return view
    }
}