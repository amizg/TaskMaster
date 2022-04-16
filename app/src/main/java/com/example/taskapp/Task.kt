package com.example.taskapp

import com.example.taskapp.notifications.Notification


class Task(
    tid: Int, nm: String, dsc: String, ddln: Long, cmpltd: Int, repeatable: Int, var mon: Int,
    var tue: Int, var wed: Int, var thu: Int, var fri: Int, var sat: Int, var sun: Int, dayLast: Int
) {

    private var taskId:Int = tid
    private var name:String = nm
    private var desc:String = dsc
    private var deadline:Long = ddln
    private var completed:Int = cmpltd
    private var dayLastCompleted: Int = dayLast
    var rp = repeatable

    init {
        MainActivity.um.scheduleNotifications(
            deadline,
            name,
            tid
        )
    }

    fun getTaskId():Int{
        return taskId
    }
    fun getName():String {
        return name
    }
    fun getDesc():String {
        return desc
    }
    fun getDeadline():Long {
        return deadline
    }
    fun getCompleted():Int{
        return completed
    }
    fun setCompleted(cmpltd: Int){
        completed = cmpltd
    }
    fun setLastCompleted(day: Int){
        dayLastCompleted = day
    }
    fun getLastCompleted(): Int{
        return dayLastCompleted
    }
}