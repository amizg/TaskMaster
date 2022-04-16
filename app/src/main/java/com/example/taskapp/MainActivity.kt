package com.example.taskapp
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.taskapp.notifications.*
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    // refresh function to add fragment changes
    companion object{
        lateinit var adapter: ViewPagerAdapter
        lateinit var viewpager:ViewPager2
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

        um = UpdateManager(this)
        //um.showPreferences()

        //requires version check as we allow older versions than when notification library was supported
        weakActivity = WeakReference<MainActivity>(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            um.createNotificationChannel()
        }
    }
}
