package com.example.taskapp

class Task(tid:Int, cid:Int, nm: String, dsc: String, ddln: Long, crtd: Long, cmpltd: Int, repeatable: Int,
           mon: Int, tue: Int, wed: Int, thu: Int, fri: Int, sat: Int, sun: Int, dayLast: Int) {
    private var taskId:Int = tid
    private var cardId:Int = cid
    private var name:String = nm
    private var desc:String = dsc
    private var deadline:Long = ddln
    private var created:Long = crtd
    private var completed:Int = cmpltd
    private var dayLastCompleted: Int = dayLast
    var rp = repeatable
    var mon = mon
    var tue = tue
    var wed = wed
    var thu = thu
    var fri = fri
    var sat = sat
    var sun = sun

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