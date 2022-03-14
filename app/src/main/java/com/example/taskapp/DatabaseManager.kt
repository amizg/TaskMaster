package com.example.taskapp
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private val TAG: String = DatabaseManager::class.java.simpleName //Debugging tag

class DatabaseManager(var context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {
    companion object {
        private const val DB_NAME = "taskmaster"
        private const val DB_VER = 6

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


    override fun onCreate(db: SQLiteDatabase?) {
        val createCardTable = "CREATE TABLE $TBL_CARDS($COL_CID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_CNAME TEXT)"
        val createTaskTable = "CREATE TABLE $TBL_TASKS ($COL_TID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_TCARD_ID INTEGER, $COL_TNAME TEXT(100), $COL_TDESC TEXT(100), $COL_TDEADLINE INTEGER, $COL_TCREATED INTEGER)"
        db?.execSQL(createCardTable)
        db?.execSQL(createTaskTable)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_CARDS")
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_TASKS")
        onCreate(db)
    }

    fun addCard(name : String){
        val values = ContentValues()
        values.put(COL_CNAME, name)
        val db = this.writableDatabase
        db.insert(TBL_CARDS, null, values)
        db.close()
    }

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

    @SuppressLint("Range")
    @Throws(SQLiteException::class)
    fun getCards(): ArrayList<Card>{
        val cards = ArrayList<Card>()
        val db = writableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery("SELECT * FROM " + TBL_CARDS, null)
        }catch (e: SQLiteException) {
            onCreate(db)
            return ArrayList()
        }
        var name: String
        //var cardId: Int?

        if(cursor!!.moveToFirst()){
            while (cursor.isAfterLast == false){

                name = cursor.getString(cursor.getColumnIndex(COL_CNAME))
                cards.add(Card(name))
            }
        }
        return cards
    }

}