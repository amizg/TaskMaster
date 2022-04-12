package com.example.taskapp
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.*
import java.util.*
import kotlin.collections.ArrayList

//For debugging Log.d(TAG,"")
private val TAG: String = DataManager::class.java.simpleName //Debugging tag

class DataManager(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {
//    Tables in our database taskmaster
//    cards: (id, name)
//    tasks: (id, card_id, name, description, deadline, created)
    companion object {
        private const val DB_NAME = "taskmaster"
        private const val DB_VER = 2

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
        private const val COL_TCREATED = "created"
        private const val COL_TCOMPLETED = "completed"
    }

    //Primary array of card objects
    private var cards:ArrayList<Card> = ArrayList()
    private var tasks:ArrayList<Task> = ArrayList()

//On creation of the database for the very first time
    //Create tasks table
    //Create cards table
    //Execute SQL statements
    override fun onCreate(db: SQLiteDatabase?) {
        val createCardTable = "CREATE TABLE $TBL_CARDS($COL_CID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_CNAME TEXT)"
        val createTaskTable = "CREATE TABLE $TBL_TASKS ($COL_TID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_TCARD_ID INTEGER, $COL_TNAME TEXT(100), $COL_TDESC TEXT(100), $COL_TDEADLINE LONG, $COL_TCREATED LONG, $COL_TCOMPLETED INTEGER)"
        db?.execSQL(createCardTable)
        db?.execSQL(createTaskTable)
    }

    //When DB_VER is incremented by 1, this will reset the entire database and its data
    //dropping both tasks and cards table
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_CARDS")
        db?.execSQL("DROP TABLE IF EXISTS $TBL_TASKS")
        onCreate(db)
    }

    //When DB_VER is incremented by 1, this will reset the entire database and its data
    //dropping both tasks and cards table
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
    fun addTask(card_id:Int, name:String, desc:String, deadline:Long, completed: Int){
        val values = ContentValues()
        values.put(COL_TCARD_ID, card_id)
        values.put(COL_TNAME, name)
        values.put(COL_TDESC, desc)
        values.put(COL_TDEADLINE, deadline)
        values.put(COL_TCREATED, System.currentTimeMillis())
        values.put(COL_TCOMPLETED, completed) //get this from the xml
        val db = this.writableDatabase
        db.insert(TBL_TASKS, null, values)
        db.close()
    }

    //Edit contents of an existing task by TaskId
    fun editTask(newTitle: String, newDesc: String, newDeadline: Long, taskId: Int){

        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COL_TNAME, newTitle)
        values.put(COL_TDESC, newDesc)
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
    fun readCards() {
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
    }

    //Returns an arraylist of cards
    fun getCards():ArrayList<Card>{
        readCards()
        return cards
    }

    fun readTask(){
        tasks.clear()
        val db = this.readableDatabase

        val cursorTasks = db.rawQuery("SELECT * FROM $TBL_TASKS", null)
        if (cursorTasks.moveToFirst()) {
            do {
                tasks.add(
                    Task(
                        cursorTasks.getInt(0),
                        cursorTasks.getInt(1),
                        cursorTasks.getString(2),
                        cursorTasks.getString(3),
                        cursorTasks.getLong(4),
                        cursorTasks.getLong(5),
                        cursorTasks.getInt(6)
                    )
                )
            } while (cursorTasks.moveToNext())
        }
        cursorTasks.close()

        for (task in tasks){
            val taskcardId = task.getCardId()
            for(card in cards){
                val cardId = card.getId()
                if(taskcardId == cardId){
                    card.addTask(task)
                }
            }
        }
        db.close()
    }
    //Returns an arraylist of tasks
    fun getTasks():ArrayList<Task>{
        return tasks
    }

    fun getCardTasks(cardId: Int): ArrayList<Task>{
        tasks.clear()
        val db = this.readableDatabase

        val cursorTasks = db.rawQuery("SELECT * FROM $TBL_TASKS WHERE $COL_TCARD_ID = $cardId ORDER BY $COL_TCOMPLETED ASC, $COL_TDEADLINE = 0, $COL_TDEADLINE ASC, $COL_TNAME ASC", null)
        if (cursorTasks.moveToFirst()) {
            do {
                tasks.add(
                    Task(
                        cursorTasks.getInt(0),
                        cursorTasks.getInt(1),
                        cursorTasks.getString(2),
                        cursorTasks.getString(3),
                        cursorTasks.getLong(4),
                        cursorTasks.getLong(5),
                        cursorTasks.getInt(6)
                    )
                )
            } while (cursorTasks.moveToNext())
        }
        cursorTasks.close()

        db.close()
        return tasks
    }
    fun markCompleted(tid: Int, completed: Int, task: Task){
        val values = ContentValues()
        val db=this.writableDatabase

        if (completed==0){
            values.put(COL_TCOMPLETED, 1)
            task.setCompleted(1)

        }
        else if (completed==1){
            values.put(COL_TCOMPLETED, 0)
            task.setCompleted(0)

        }
        db.update(TBL_TASKS, values, "$COL_TID=?", arrayOf(tid.toString()))
        db.close()
    }

    fun dagTasks(): ArrayList<Task>{

        tasks.clear()
        val db = this.readableDatabase

        val date = Date()
        val start = getStartOfDay(date)
        val end = getEndOfDay(date)

        // TODO ????????????
        // Design: leave out passed tasks?
        // Call "Day Ahead" or "Tasks for Today"?

        val cursorTasks = db.rawQuery("SELECT * FROM $TBL_TASKS WHERE $COL_TCOMPLETED=0 AND " +
                                            "$COL_TDEADLINE>$start AND $COL_TDEADLINE<$end OR $COL_TDEADLINE=0 " +
                                             "ORDER BY $COL_TDEADLINE = 0, $COL_TDEADLINE ASC, $COL_TNAME ASC", null)

        if (cursorTasks.moveToFirst()) {
            do {
                tasks.add(
                    Task(
                        cursorTasks.getInt(0),
                        cursorTasks.getInt(1),
                        cursorTasks.getString(2),
                        cursorTasks.getString(3),
                        cursorTasks.getLong(4),
                        cursorTasks.getLong(5),
                        cursorTasks.getInt(6)
                    )
                )
            } while (cursorTasks.moveToNext())
        }
        cursorTasks.close()

        db.close()
        return tasks
    }

    fun getStartOfDay(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

    fun getEndOfDay(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999
        return calendar.timeInMillis
    }

}
