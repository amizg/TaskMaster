package com.example.taskapp.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.ViewPagerAdapter
import me.relex.circleindicator.CircleIndicator3

class AddCardFragment : Fragment() {

    private val alertDialog = MainActivity.alertBuilder //for building popup screens

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View = inflater.inflate(R.layout.fragment_add_card, container, false)

        val addCardBtn:Button = view.findViewById(R.id.addCardBtn)

        addCardBtn.setOnClickListener{
            addCardBox()
        }
        return view
    }

    // alertDialog for adding a card
    private fun addCardBox() {

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_box_edittext, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)

        alertDialog.setView(dialogLayout)
        alertDialog.setTitle("Enter Card Name")

        alertDialog.setPositiveButton("Enter") { _, _ ->
            MainActivity.dm.addCard(editText.text.toString())
            refreshCard(MainActivity.dm.readCards().size + 1)
        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.setNeutralButton(""){dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshCard(pos: Int){
        MainActivity.adapter = ViewPagerAdapter(MainActivity.fm, lifecycle)
        MainActivity.viewpager = MainActivity.viewpager.findViewById(R.id.viewpager)
        MainActivity.viewpager.adapter = MainActivity.adapter
        MainActivity.indicator.setViewPager(MainActivity.viewpager)

        MainActivity.adapter.notifyDataSetChanged()
        MainActivity.viewpager.setCurrentItem(pos - 1, false)
    }
}