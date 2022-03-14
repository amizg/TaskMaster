package com.example.taskapp
import kotlin.collections.ArrayList

class Card(nm: String) {
    private var name = nm
    private var tasks:ArrayList<Task> = ArrayList()

    fun setName(nm:String){
        name = nm
    }
    fun getName():String{
        return name
    }
    fun addTask(nm:String, dsc:String, date:Long){
        val task = Task(nm,dsc,date)
        tasks.add(task)
    }
}