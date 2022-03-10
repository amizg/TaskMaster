package com.example.taskapp


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



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
