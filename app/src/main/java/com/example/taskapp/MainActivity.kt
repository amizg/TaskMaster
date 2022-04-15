package com.example.taskapp
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
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.taskapp.databinding.ActivityMainBinding
import com.example.taskapp.notifications.*
import com.example.taskapp.Fragments.RecyclerAdapter
import java.lang.ref.WeakReference
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

//For debugging Log.d(TAG,"")
private val TAG: String = MainActivity::class.java.simpleName //Debugging tag

class MainActivity : AppCompatActivity() {



    // refresh function to add fragment changes
    companion object{
        lateinit var weakActivity: WeakReference<MainActivity>
        lateinit var rcy: RecyclerAdapter
        lateinit var adapter: ViewPagerAdapter
        lateinit var viewpager:ViewPager2
        lateinit var dm:DataManager
        lateinit var fm: FragmentManager
        lateinit var um: UpdateManager
        lateinit var alertBuilder: AlertDialog.Builder
        fun getmInstanceActivity(): MainActivity? {
            return weakActivity.get()
        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("E h:mm a \n 'on' \n MM/dd/yyyy")
            return format.format(date)
        }

        fun convertDateToLong(date: String): Long {
            val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
            return df.parse(date).time
        }

        fun covertLongToSimpleTime(time: Long): String{
            val date = Date(time)
            val format = SimpleDateFormat("MMM d \n h:mm a")
            return format.format(date)
        }

        fun covertLongToSimpleDate(time: Long): String{
            val date = Date(time)
            val format = SimpleDateFormat("yyyyMMdd")
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

        //requires version check as we allow older versions than when notification library was supported
        weakActivity = WeakReference<MainActivity>(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            um.createNotificationChannel()
        }
    }
}
