package com.example.taskapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.taskapp.notifications.Notification
import java.time.*
import java.util.*
import kotlin.collections.ArrayList

class DataManager(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {
//    Tables in our database taskmaster
//    cards: (id, name)
//
//    tasks: (id, card_id, name, description, deadline, completed,
//    repeat, days of the week, day last completed)

    companion object {
        private const val DB_NAME = "taskmaster"
        private const val DB_VER = 1

        //card table
        private const val TBL_CARDS = "cards"
        private const val COL_CID = "id"
        private const val COL_CNAME = "name"

        //tasks table
        private const val TBL_TASKS = "tasks"
        private const val COL_TID = "id"
        private const val COL_TCARD_ID = "card_id"
        private const val COL_TNAME = "name"
        private const val COL_TDESC = "description"
        private const val COL_TDEADLINE = "deadline"
        private const val COL_TCOMPLETED = "completed"
        private const val COL_TRP = "repeat"
        private const val COL_TMON = "monday"
        private const val COL_TTUE = "tuesday"
        private const val COL_TWED = "wednesday"
        private const val COL_TTHU = "thursday"
        private const val COL_TFRI = "friday"
        private const val COL_TSAT = "saturday"
        private const val COL_TSUN = "sunday"
        private const val COL_TLASTCOMPLETED = "last_completed"
    }

    //Primary array of card objects
    private var cards:ArrayList<Card> = ArrayList()

    //Primary array of task objects
    private var tasks:ArrayList<Task> = ArrayList()

//On creation of the database for the very first time
    //Create tasks table
    //Create cards table
    //Create notifications table
    //Execute SQL statements
    override fun onCreate(db: SQLiteDatabase?) {
        val createCardTable = "CREATE TABLE $TBL_CARDS($COL_CID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_CNAME TEXT)"

        val createTaskTable = "CREATE TABLE $TBL_TASKS ($COL_TID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_TCARD_ID INTEGER, $COL_TNAME TEXT(100), $COL_TDESC TEXT(100)," +
                " $COL_TDEADLINE INTEGER, $COL_TCOMPLETED INTEGER, $COL_TRP INTEGER, $COL_TMON INTEGER, $COL_TTUE INTEGER, $COL_TWED INTEGER, " +
                "$COL_TTHU INTEGER, $COL_TFRI INTEGER, $COL_TSAT INTEGER, $COL_TSUN INTEGER, $COL_TLASTCOMPLETED INTEGER)"

        db?.execSQL(createCardTable)
        db?.execSQL(createTaskTable)
    }

    //When DB_VER is incremented by 1, this will reset the entire database and its data
    //dropping both tasks and cards table
    //TODO: Remove before turn-in?
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_CARDS")
        db?.execSQL("DROP TABLE IF EXISTS $TBL_TASKS")
        onCreate(db)
    }

    //When DB_VER is decremented by 1, this will reset the entire database and its data
    //dropping both tasks and cards table
    //TODO: Remove before turn-in?
    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_CARDS")
        db?.execSQL("DROP TABLE IF EXISTS $TBL_TASKS")
        onCreate(db)
    }

    //Adding a card to the database
    fun addCard(name : String){
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COL_CNAME, name)
        db.insert(TBL_CARDS, null, values)
        db.close()
    }

    //Edit Card name column for existing Card by Card Name
    fun editCard(newName: String, cardId: Int){
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COL_CNAME, newName)
        db.update(TBL_CARDS, values, "$COL_CID=?", arrayOf(cardId.toString()))
        db.close()
    }

    //Delete Card
    fun deleteCard(cardId: Int){

        val db = this.writableDatabase

        db.delete(TBL_CARDS, "$COL_CID=?", arrayOf(cardId.toString()))
        db.delete(TBL_TASKS, "$COL_TCARD_ID=?", arrayOf(cardId.toString()))
        db.close()
    }

    //Adding a task to the database
    fun addTask(card_id:Int, name:String, desc:String, deadline:Long, completed: Int, rp: Int,
                mon: Int, tues: Int, wed: Int, thu: Int, fri: Int, sat: Int, sun: Int, dayLast: Int){

        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COL_TCARD_ID, card_id)
        values.put(COL_TNAME, name)
        values.put(COL_TDESC, desc)
        values.put(COL_TDEADLINE, deadline)
        values.put(COL_TCOMPLETED, completed) //get this from the xml
        values.put(COL_TRP, rp)
        values.put(COL_TMON, mon)
        values.put(COL_TTUE, tues)
        values.put(COL_TWED, wed)
        values.put(COL_TTHU, thu)
        values.put(COL_TFRI, fri)
        values.put(COL_TSAT, sat)
        values.put(COL_TSUN, sun)
        values.put(COL_TLASTCOMPLETED, dayLast)

        db.insert(TBL_TASKS, null, values)
        db.close()
    }

    //Edit contents of an existing task by TaskId
    fun editTask(newTitle: String, newDesc: String, newDeadline: Long, completed: Int, taskId: Int, rp: Int,
                 mon: Int, tues: Int, wed: Int, thu: Int, fri: Int, sat: Int, sun: Int, dayLast: Int){
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COL_TNAME, newTitle)
        values.put(COL_TDESC, newDesc)
        values.put(COL_TDEADLINE, newDeadline)
        values.put(COL_TCOMPLETED, completed)
        values.put(COL_TRP, rp)
        values.put(COL_TMON, mon)
        values.put(COL_TTUE, tues)
        values.put(COL_TWED, wed)
        values.put(COL_TTHU, thu)
        values.put(COL_TFRI, fri)
        values.put(COL_TSAT, sat)
        values.put(COL_TSUN, sun)
        values.put(COL_TLASTCOMPLETED, dayLast)
        values.put(COL_TDEADLINE, newDeadline)

        db.update(TBL_TASKS, values, "$COL_TID=?", arrayOf(taskId.toString()))
        db.close()
    }

    //Delete Task
    fun deleteTask(taskId: Int){

        val db = this.writableDatabase

        db.delete(TBL_TASKS, "$COL_TID=?", arrayOf(taskId.toString()))
        db.close()
    }

    //Reading tasks from the database, importing for reassigning the cards arraylist
    fun readCards(): ArrayList<Card> {
        cards.clear()
        val db = this.readableDatabase

        val cursorCards = db.rawQuery("SELECT * FROM $TBL_CARDS", null)
        if (cursorCards.moveToFirst()) {
            do {
                cards.add(
                    Card(
                        cursorCards.getInt(0),
                        cursorCards.getString(1)
                    )
                )
            } while (cursorCards.moveToNext())
        }
        cursorCards.close()
        db.close()
        return cards
    }

    // gets repeatable tasks to display on routines fragment
    fun getRoutines():ArrayList<Task>{
        tasks.clear()

        val db = this.readableDatabase
        val cursorTasks = db.rawQuery("SELECT * FROM $TBL_TASKS WHERE $COL_TRP = 1", null)

        if (cursorTasks.moveToFirst()) {
            do {
                tasks.add(
                    Task(
                        cursorTasks.getInt(0),
                        cursorTasks.getString(2),
                        cursorTasks.getString(3),
                        cursorTasks.getLong(4),
                        cursorTasks.getInt(5),
                        cursorTasks.getInt(6),
                        cursorTasks.getInt(7),
                        cursorTasks.getInt(8),
                        cursorTasks.getInt(9),
                        cursorTasks.getInt(10),
                        cursorTasks.getInt(11),
                        cursorTasks.getInt(12),
                        cursorTasks.getInt(13),
                        cursorTasks.getInt(14)
                    )
                )
            } while (cursorTasks.moveToNext())
        }
        cursorTasks.close()
        db.close()
        return tasks
    }

    // gets tasks with id matching current card to be displayed in recyclerview
    fun getCardTasks(cardId: Int): ArrayList<Task>{
        // check each task in the ArrayList
        for(Task in tasks)
        {
            // check if task is repeatable and marked complete
            if (Task.rp==1 && Task.getCompleted()==1)
            {
                // check if current day is not the same as the last day task was completed
                if(getDay()!=Task.getLastCompleted())
                {
                    // mark the task incomplete for the next time the task appears
                    editTask(Task.getName(),Task.getDesc(),Task.getDeadline(),0 ,Task.getTaskId(),
                        0 , Task.mon,Task.tue,Task.wed,Task.thu,Task.fri,Task.sat,Task.sun, Task.getLastCompleted())
                }
            }
        }
        tasks.clear()

        val db = this.readableDatabase
        val dayQuery = dayQuery(getDay())
        val cursorTasks = db.rawQuery("SELECT * FROM $TBL_TASKS WHERE (($dayQuery AND $COL_TRP = 1) OR $COL_TRP = 0) " +
                "AND $COL_TCARD_ID = $cardId ORDER BY $COL_TCOMPLETED ASC, $COL_TDEADLINE = 0, $COL_TDEADLINE ASC, $COL_TNAME ASC", null)

        if (cursorTasks.moveToFirst()) {
            do {
                tasks.add(
                    Task(
                        cursorTasks.getInt(0),
                        cursorTasks.getString(2),
                        cursorTasks.getString(3),
                        cursorTasks.getLong(4),
                        cursorTasks.getInt(5),
                        cursorTasks.getInt(6),
                        cursorTasks.getInt(7),
                        cursorTasks.getInt(8),
                        cursorTasks.getInt(9),
                        cursorTasks.getInt(10),
                        cursorTasks.getInt(11),
                        cursorTasks.getInt(12),
                        cursorTasks.getInt(13),
                        cursorTasks.getInt(14)
                    )
                )
            } while (cursorTasks.moveToNext())
        }
        cursorTasks.close()
        db.close()
        return tasks
    }

    // takes in current day as integer and returns SQL query
    private fun dayQuery(currentDay:Int):String{
        return when (currentDay) {
            1 -> "$COL_TSUN=1"
            2 -> "$COL_TMON=1"
            3 -> "$COL_TTUE=1"
            4 -> "$COL_TWED=1"
            5 -> "$COL_TTHU=1"
            6 -> "$COL_TFRI=1"
            7 -> "$COL_TSAT=1"
            else -> ""
        }
    }

    // creates calendar instance and returns current day based on system time zone
    private fun getDay():Int{
        // calendar
        val cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getDefault()

        return cal.get(Calendar.DAY_OF_WEEK)
    }

    // marks task completed in database
    fun markCompleted(tid: Int, completed: Int, task: Task){
        val values = ContentValues()

        val db = this.writableDatabase

        if (completed==0){
            values.put(COL_TCOMPLETED, 1)
            task.setCompleted(1)
            // set last completed to current day
            values.put(COL_TLASTCOMPLETED, getDay())
            task.setLastCompleted(getDay())
        }
        else if (completed==1){
            values.put(COL_TCOMPLETED, 0)
            task.setCompleted(0)
        }
        db.update(TBL_TASKS, values, "$COL_TID=?", arrayOf(tid.toString()))
        db.close()
    }

    // deletes completed, non-repeatable tasks from the current card
    fun clearCompleted(cardId: Int){
        val db = this.writableDatabase

        db.delete(TBL_TASKS, "$COL_TRP=0 AND $COL_TCOMPLETED=1 AND $COL_TCARD_ID=?", arrayOf(cardId.toString()))

    }

    // deletes completed, non-repeatable tasks from all cards
    fun clearAllComplete(){
        val db = this.writableDatabase

        db.delete(TBL_TASKS, "$COL_TRP=0 AND $COL_TCOMPLETED=?", arrayOf(1.toString()))
    }

    // gets tasks with deadlines on the current day, and tasks that are repeatable on the current day
    fun dagTasks(): ArrayList<Task>{
        tasks.clear()
        val db = this.readableDatabase

        val date = Date()
        val start = getStartOfDay(date)
        val end = getEndOfDay(date)
        val dayQuery = dayQuery(getDay())

        val cursorTasks = db.rawQuery("SELECT * FROM $TBL_TASKS WHERE $COL_TCOMPLETED=0 AND " +
                                            "($COL_TDEADLINE>$start AND $COL_TDEADLINE<$end) OR " +
                                            "($dayQuery AND $COL_TRP = 1) " +
                                            "ORDER BY $COL_TDEADLINE = 0, $COL_TDEADLINE ASC, $COL_TNAME ASC", null)

        if (cursorTasks.moveToFirst()) {
            do {
                tasks.add(
                    Task(
                        cursorTasks.getInt(0),
                        cursorTasks.getString(2),
                        cursorTasks.getString(3),
                        cursorTasks.getLong(4),
                        cursorTasks.getInt(5),
                        cursorTasks.getInt(6),
                        cursorTasks.getInt(7),
                        cursorTasks.getInt(8),
                        cursorTasks.getInt(9),
                        cursorTasks.getInt(10),
                        cursorTasks.getInt(11),
                        cursorTasks.getInt(12),
                        cursorTasks.getInt(13),
                        cursorTasks.getInt(14)
                    )
                )
            } while (cursorTasks.moveToNext())
        }
        cursorTasks.close()

        db.close()
        return tasks
    }

    // finds 12:00:00 AM of current day, to long
    private fun getStartOfDay(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

    // finds 11:59:59 PM of current day, to long
    private fun getEndOfDay(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999
        return calendar.timeInMillis
    }
}