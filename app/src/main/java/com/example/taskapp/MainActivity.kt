package com.example.taskapp

import android.os.Bundle
import android.provider.ContactsContract
import android.service.quickaccesswallet.GetWalletCardsCallback
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.collections.ArrayList

private val TAG: String = MainActivity::class.java.simpleName //Debugging tag
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaring and setting up the database
        val db:DataManager = DataManager(this)

        //Initializing the view pager adapter
        var adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        //assigning the layout ViewPager2 to viewpager with the id of viewpager
        val viewpager = findViewById<ViewPager2>(R.id.viewpager)
        viewpager.adapter = adapter

        //Cards must be read from the database
        //Before accessing any data, the database must be read to set local variables
        db.readCards()
        //the cards are set for the viewpager
        //Viewpager has its own local storage for card objects
        adapter.setCards(db.getCards())
    }
}
