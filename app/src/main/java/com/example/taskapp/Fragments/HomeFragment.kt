package com.example.taskapp.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.taskapp.Card
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.ViewPagerAdapter


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clearAllCmpltBtn: View = view.findViewById(R.id.clearAllCompleted)

        clearAllCmpltBtn.setOnClickListener {
            MainActivity.dm.clearAllComplete()
            refreshAllCards()

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun refreshAllCards(){
        MainActivity.adapter = ViewPagerAdapter(MainActivity.fm, lifecycle)
        MainActivity.viewpager = MainActivity.viewpager.findViewById(R.id.viewpager)
        MainActivity.viewpager.adapter = MainActivity.adapter

        MainActivity.adapter.notifyDataSetChanged()
    }


}