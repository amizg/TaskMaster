package com.example.taskapp

class Task {
    private var name:String = ""
    private var desc:String = ""
    private var deadline:Long = 0
    private var created:Long = 0

    constructor(nm:String, dsc:String, date:Long){
        name = nm
        desc = dsc
        deadline = date
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