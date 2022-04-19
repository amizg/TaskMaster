package com.example.TaskMaster

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.TaskMaster.notifications.*
import me.relex.circleindicator.CircleIndicator3
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaring and setting up the database
        dm = DataManager(this)
        alertBuilder = AlertDialog.Builder(this, R.style.AlertDialogTheme)

        //Initializing the view pager adapter
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        fm = supportFragmentManager

        //assigning the layout ViewPager2 to viewpager with the id of viewpager
        viewpager = findViewById(R.id.viewpager)
        viewpager.adapter = adapter

        //IndicatorView at bottom
        indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(viewpager)

        um = UpdateManager(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            um.createNotificationChannel()
        }
    }

    // refresh function to add fragment changes
    companion object{
        lateinit var adapter: ViewPagerAdapter
        lateinit var viewpager:ViewPager2
        lateinit var indicator:CircleIndicator3
        lateinit var dm:DataManager
        lateinit var fm: FragmentManager
        lateinit var um: UpdateManager
        lateinit var alertBuilder: AlertDialog.Builder

        @SuppressLint("SimpleDateFormat")
        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("E h:mm a \n 'on' \n MM/dd/yyyy")
            return format.format(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun convertDateToLong(date: String): Long {
            val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
            return df.parse(date).time
        }

        @SuppressLint("SimpleDateFormat")
        fun convertLongToSimpleTime(time: Long): String{
            val date = Date(time)
            val format = SimpleDateFormat("MMM d \n h:mm a")
            return format.format(date)
        }

        fun currentTimeToLong(): Long {
            return System.currentTimeMillis()
        }
    }
}
