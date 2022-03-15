package com.example.taskapp
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val TAG: String = DataManager::class.java.simpleName //Debugging tag

class DataManager(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {
//    Tables in our database taskmaster
//    cards: (id, name)
//    tasks: (id, card_id, name, description, deadline, created)
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
        private const val COL_TCREATED = "created"
    }

    //Primary array of card objects
    private var cards:ArrayList<Card> = ArrayList()

//On creation of the database for the very first time
    //Create tasks table
    //Create cards table
    //Execute SQL statements
    override fun onCreate(db: SQLiteDatabase?) {
        val createCardTable = "CREATE TABLE $TBL_CARDS($COL_CID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_CNAME TEXT)"
        val createTaskTable = "CREATE TABLE $TBL_TASKS ($COL_TID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_TCARD_ID INTEGER, $COL_TNAME TEXT(100), $COL_TDESC TEXT(100), $COL_TDEADLINE INTEGER, $COL_TCREATED INTEGER)"
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
        val values = ContentValues()
        values.put(COL_CNAME, name)
        val db = this.writableDatabase
        db.insert(TBL_CARDS, null, values)
        db.close()
    }

    //Adding a task to the database
    fun addTask(card_id:Int, name:String, desc:String, deadline:Long, created:Long){
        val values = ContentValues()
        values.put(COL_TCARD_ID, card_id)
        values.put(COL_TNAME, name)
        values.put(COL_TDESC, desc)
        values.put(COL_TDEADLINE, deadline)
        values.put(COL_TCREATED, created)
        val db = this.writableDatabase
        db.insert(TBL_TASKS, null, values)
        db.close()
    }

    //Reading tasks from the database, importing for reassigning the cards arraylist
    fun readCards() {
        val db = this.readableDatabase

        val cursorCards = db.rawQuery("SELECT * FROM $TBL_CARDS", null)
        if (cursorCards.moveToFirst()) {
            do {
                cards.add(
                    Card(
                        cursorCards.getString(1)
                    )
                )
            } while (cursorCards.moveToNext())
        }
        cursorCards.close()
    }

    //Returns an arraylist of cards
    fun getCards():ArrayList<Card>{
        return cards
    }

    fun clearCards(){
        cards.clear()
    }
}