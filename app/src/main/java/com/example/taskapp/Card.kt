package com.example.taskapp
import com.example.taskapp.Task
import java.util.*
import kotlin.collections.ArrayList

class Card {
    private var name = ""
    private var tasks:ArrayList<Task> = ArrayList()

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