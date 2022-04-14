package com.example.taskapp
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.taskapp.databinding.ActivityMainBinding
import com.example.taskapp.notifications.*
import java.text.SimpleDateFormat
import java.util.*

//For debugging Log.d(TAG,"")
private val TAG: String = MainActivity::class.java.simpleName //Debugging tag

class MainActivity : AppCompatActivity() {

    // refresh function to add fragment changes
    companion object{
        lateinit var adapter: ViewPagerAdapter
        lateinit var viewpager:ViewPager2
        lateinit var dm:DataManager
        lateinit var fm: FragmentManager
        lateinit var alertBuilder: AlertDialog.Builder


        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("E hh:mm a \n 'on' \n MM/dd/yyyy")
            return format.format(date)
        }

        fun convertDateToLong(date: String): Long {
            val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
            return df.parse(date).time
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
        alertBuilder = AlertDialog.Builder(this)

        //Initializing the view pager adapter
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        fm = supportFragmentManager
        //assigning the layout ViewPager2 to viewpager with the id of viewpager
        viewpager = findViewById(R.id.viewpager)
        viewpager.adapter = adapter

        createNotificationChannel()
        scheduleNotification()
    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = "testing"
        val message = "123"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        //set time to like one minute after current instance to notify with text message
        //val time = calendar.set()
    }

    private fun createNotificationChannel() {
        val name = "Day at a Glance Channel"
        val desc = "Category of Day at a Glance notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
