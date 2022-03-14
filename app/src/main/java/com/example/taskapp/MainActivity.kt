package com.example.taskapp

import android.os.Bundle
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

        var cards: CardManager = CardManager()

        val db = DatabaseManager(this)
        //db.addCard("Work")
        //db.addTask(1,"Do your homework","CSC 302",1654056000000, System.currentTimeMillis())

        val debugtimestamp:Long = 1654056000000

        var adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        val viewpager = findViewById<ViewPager2>(R.id.viewpager)

        viewpager.adapter = adapter



        //Adds Card to Card List and Database
        fun addCard(name: String){
            //cards.addCard(name)
            //db.addCard(name)
        }

        //addCard("Work")
        //addCard("School")
        //addCard("Home")

        cards.setCards(db.getCards())
        adapter.setCards(cards)



        //cards.removeCard(2)
        //cards.editCard("PooPoo", 2)


    }



}
