package com.example.TaskMaster.notifications

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