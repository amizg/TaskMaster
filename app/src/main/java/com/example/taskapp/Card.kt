package com.example.taskapp
import com.example.taskapp.Task
import java.util.*

class Card {
    private var name = ""
    private var tasks:Vector<Task> = Vector()

    constructor(nm:String){
        name = nm
    }

    fun setName(nm:String){
        name = nm
    }
    fun getName():String{
        return name
    }
    fun addTask(nm:String, dsc:String, date:Long){
        var task = Task(nm,dsc,date)
        tasks.add(task)
    }
}