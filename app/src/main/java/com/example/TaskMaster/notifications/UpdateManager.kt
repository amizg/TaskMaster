package com.example.TaskMaster.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.example.TaskMaster.MainActivity
import java.util.*

const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
const val preferenceKey = "taskmaster_key"
const val lastNotifPrefKey = "last_notification_preference_key"

class UpdateManager (val context: Context) {
    val alarmManager = getSystemService(context, AlarmManager::class.java) as AlarmManager
    private var currentNotificationID = 0

    fun getCurrentNotifID(): Int {
        return currentNotificationID
    }

    //required for notification, is category of notifications and requires a minimum build version
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val name = "Task Notification Channel"
        val desc = "Task deadline warning notifications."
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(context, NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    //creates notification and sets it using AlarmManager
    fun scheduleNotification(ttl: String, msg: String, tm: Long, taskID: Int){
        currentNotificationID = getNextNotifID()

        val intent = Intent(context, AlarmReceiver::class.java)

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

        //add notif to shared prefs in format "id,id,...id,"
        val sp = context.getSharedPreferences(preferenceKey, MODE_PRIVATE)
        sp.edit().putString(taskID.toString(), sp.getString(taskID.toString(), "") + currentNotificationID + ",").apply()

        //TODO testing purposes only - shows SP key and notification IDs within
        println(taskID.toString() + ": " + sp.getString(taskID.toString(), "taskID key not found"))
    }

    //TODO does not require API
    fun scheduleNotifications(deadline: Long, name: String, taskID: Int) {
        //if there are currently notifications in the shared preferences for the task ID, do not create new ones
        val sp = context.getSharedPreferences(preferenceKey, MODE_PRIVATE)
        if (sp.getString(taskID.toString(), null) != null) return

        if (!deadline.equals(0)) {
            if (deadline < MainActivity.currentTimeToLong()) {
                //deadline has already passed, notify deadline reached
                scheduleNotification(
                    "Deadline Reached",
                    name,
                    MainActivity.currentTimeToLong(),
                    taskID
                )
            } else if (deadline - 3600000 < MainActivity.currentTimeToLong()) {
                //deadline already less than an hour from passing, notify approaching & notify deadline reached when time
                scheduleNotification(
                    "Deadline Approaching",
                    name,
                    MainActivity.currentTimeToLong(),
                    taskID
                )

                scheduleNotification(
                    "Deadline Reached",
                    name,
                    deadline,
                    taskID
                )
            } else {
                //TODO schedule DAG notifs; wake time, sleep time

                //schedule approaching & reached deadlines
                scheduleNotification(
                    "Deadline Approaching",
                    name,
                    deadline - 3600000,
                    taskID
                )

                scheduleNotification(
                    "Deadline Reached",
                    name,
                    deadline,
                    taskID
                )
            }

            //TODO testing
            //showNotifications()
            //showPreferences()
        }
    }

    //Cancel notifications by taskID; cancels notifications then clears from SharedPreferences
    fun cancelTaskNotifications(taskID: Int) {
        val sp = context.getSharedPreferences(preferenceKey, MODE_PRIVATE)
        var notifIDs = ArrayList<Int>()
        notifIDs.clear()

        //split the notification IDs for the task into an array list
        val stringIDs = sp.getString(taskID.toString(), null)?.split(",")
        stringIDs?.forEach { if (it != "null" && it != "") notifIDs.add(it.toInt()) }

        //for each notification ID, cancel the notification by that ID
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifIDs?.forEach { manager.cancel(it) }

        //make notifications for that task empty
        sp.edit().putString(taskID.toString(), "").apply()
    }

    //TODO clear unnecessary preferences junk
    fun clearTaskFromPreferences(taskID: Int) {
        val sp = context.getSharedPreferences(preferenceKey, MODE_PRIVATE)
        var allPrefMap = sp.all
        var correctID: String = ""

        allPrefMap.forEach {
            if (it.key != lastNotifPrefKey && it.key.toInt() == taskID) {
                correctID = it.key
                return@forEach
            }
        }

        allPrefMap.remove(correctID)

        sp.edit().clear().apply()

        allPrefMap.forEach {
            if (it.key != lastNotifPrefKey) {
                sp.edit().putString(it.key, it.value.toString()).apply()
            } else {
                sp.edit().putInt(it.key, currentNotificationID).apply()
            }
        }

        //showPreferences()
    }

    //get unique notification ID by storing one in SharedPreferences and incrementing value
    private fun getNextNotifID() : Int {
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

    //ONLY DO THIS IS YOU MESS UP THE PREFERENCES
    fun clearPrefs() {
        val sp = context.getSharedPreferences(preferenceKey, MODE_PRIVATE)
        sp.edit().clear().apply()
    }

    //TESTING PURPOSES ONLY
//    private fun showNotifications() {
//        val notificationManager = getSystemService(context, NotificationManager::class.java) as NotificationManager
//        println("VVVVVVVV NOTIFICATIONS VVVVVVVV")
//        notificationManager.activeNotifications.forEach { print(it.id) }
//    }

    //TODO TESTING PURPOSES ONLY
    fun showPreferences() {
        val sp = context.getSharedPreferences(preferenceKey, MODE_PRIVATE)
        println("VVVVVVVV PREFERENCES VVVVVVVV")
        sp.all.forEach { print(it.key.toString() + " - " + println(it.value)) }
    }
}