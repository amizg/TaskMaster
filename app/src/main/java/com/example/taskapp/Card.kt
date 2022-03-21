package com.example.taskapp
import kotlin.collections.ArrayList

class Card(id:Int, nm: String) {
    private var cid = id
    private var name = nm
    private var tasks:ArrayList<Task> = ArrayList()

    fun setName(nm:String){
        name = nm
    }
    fun getName():String{
        return name
    }
    fun setId(id:Int) {
        cid = id
    }
    fun getId():Int{
        return cid
    }
//    fun addTask(cid:Int, nm:String, dsc:String, ddln:Long, crtd:Long){
//        val task = Task(cid,nm,dsc,ddln,crtd)
//        tasks.add(task)
//    }
    fun addTask(task:Task){
        tasks.add(task)
    }
    fun getTasks():ArrayList<Task>{
        MainActivity.dm.readTask()
        return tasks
    }
}