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
import java.util.*

class Notification (nID: Int, tID: Int) {
    private var notifID:Int = nID
    private var taskID:Int = tID

    fun getNID(): Int {
        return notifID
    }

    fun getTID(): Int {
        return taskID
    }
}