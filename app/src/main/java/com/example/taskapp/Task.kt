package com.example.taskapp

class Task(tid:Int, cid:Int, nm: String, dsc: String, ddln: Long, crtd: Long) {
    private var taskId:Int = tid
    private var cardId:Int = cid
    private var name:String = nm
    private var desc:String = dsc
    private var deadline:Long = ddln
    private var created:Long = crtd

    fun getTaskId():Int{
        return taskId
    }

    fun getCardId():Int{
        return cardId
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