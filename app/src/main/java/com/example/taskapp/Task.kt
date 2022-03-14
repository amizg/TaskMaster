package com.example.taskapp

class Task(nm: String, dsc: String, date: Long) {
    private var name:String = nm
    private var desc:String = dsc
    private var deadline:Long = date
    private var created:Long = 0

    init {
        created = System.currentTimeMillis()
    }

    fun setName(nm:String){
        name = nm
    }
    fun getName():String {
        return name
    }
    fun setDesc(dsc:String){
        desc = dsc
    }
    fun getDesc():String {
        return desc
    }
    fun setDeadline(dead:Long){
        deadline = dead
    }
    fun getDeadline():Long {
        return deadline
    }
    fun getCreated():Long{
        return created
    }
}