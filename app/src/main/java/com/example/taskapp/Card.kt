package com.example.taskapp
import kotlin.collections.ArrayList

class Card(id:Int, nm: String) {
    private var cid = id
    private var name = nm
    private var tasks:ArrayList<Task> = ArrayList()

    fun getName():String{
        return name
    }
    fun getId():Int{
        return cid
    }
    fun getTasks():ArrayList<Task>{
        tasks = MainActivity.dm.getCardTasks(cid)
        return tasks
    }
}