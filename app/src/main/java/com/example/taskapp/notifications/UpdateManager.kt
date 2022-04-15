package com.example.taskapp.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.Task
import java.util.*

class UpdateManager (val context: Context) : BroadcastReceiver() {
    val alarmManager = getSystemService(context, AlarmManager::class.java) as AlarmManager
    var currentNotificationID = 0

    //required for notification, is category of notifications and requires a minimum build version
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val name = "Day at a Glance Channel"
        val desc = "Category of Day at a Glance notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(context, NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    //creates notification and sets it using AlarmManager
    fun scheduleNotification(ttl: String, msg: String, tm: Long) : Int{
        currentNotificationID = getNextNotifID(context)

        val intent = Intent(context, UpdateManager::class.java)

        intent.putExtra(titleExtra, ttl)
        intent.putExtra(messageExtra, msg)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            currentNotificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                tm,
                pendingIntent
            )
        }

        //ADD NOTIFICATION TO TABLE WITH PROPER TASK ID AND NOTIF ID

        return currentNotificationID
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(currentNotificationID, notification)
    }

    fun scheduleNotifications(deadline: Long, name: String) {
        if (!deadline.equals(0)) {
            if (deadline < MainActivity.currentTimeToLong()) {
                //deadline has already passed, notify deadline reached
                scheduleNotification(
                    "Deadline Reached",
                    name,
                    MainActivity.currentTimeToLong()
                )
            } else if (deadline - 3600000 < MainActivity.currentTimeToLong()) {
                //deadline already less than an hour from passing, notify approaching & notify deadline reached when time
                scheduleNotification(
                    "Deadline Approaching",
                    name,
                    MainActivity.currentTimeToLong()
                )

                scheduleNotification(
                    "Deadline Reached",
                    name,
                    MainActivity.currentTimeToLong()
                )
            } else {
                //schedule DAG notifs; wake time, sleep time
                //schedule approaching & reached deadlines additionally
            }
        }
    }

    //get unique notification ID by storing one in SharedPreferences and incrementing value
    private fun getNextNotifID(context: Context) : Int {
        val sp = context.getSharedPreferences(preferenceKey, MODE_PRIVATE)
        var id = sp.getInt(lastNotifPrefKey, 0) + 1;
        if (id == Integer.MAX_VALUE) id = 0
        sp.edit().putInt(lastNotifPrefKey, id).apply()
        return id
    }

    //show on-screen alert, mostly for testing purposes but can be used if we want pop-up alert
    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(context)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(context)

        AlertDialog.Builder(context)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date)
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }
}