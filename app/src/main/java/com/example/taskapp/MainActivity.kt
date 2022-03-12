package com.example.taskapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

private val TAG: String = MainActivity::class.java.simpleName //Debugging tag

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db:DatabaseManager = DatabaseManager(this)
        //db.addCard("Work")
        //db.addTask(1,"Do your homework","CSC 302",1654056000000, System.currentTimeMillis())

        val debugtimestamp:Long = 1654056000000

        var cards:Vector<Card> = Vector()


        var adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        val tablayout = findViewById<TabLayout>(R.id.tablayout)
        val viewpager = findViewById<ViewPager2>(R.id.viewpager)

        viewpager.adapter = adapter

        TabLayoutMediator(tablayout, viewpager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Home"
                }
                1 -> {
                    tab.text = "Card"
                }
                2 -> {
                    tab.text = "Test"
                }
            }
        }.attach()
    }
}
