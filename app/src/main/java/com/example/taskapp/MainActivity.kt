package com.example.taskapp
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.taskapp.AddCardFragment

//For debugging Log.d(TAG,"")
private val TAG: String = MainActivity::class.java.simpleName //Debugging tag

class MainActivity : AppCompatActivity() {

    // refresh function to add fragment changes
    fun refresh(cards:ArrayList<Card>, size:Int)
    {
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        val viewpager = findViewById<ViewPager2>(R.id.viewpager)
        viewpager.adapter = adapter
        adapter.setCards(cards)
        viewpager.currentItem = size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaring and setting up the database
        val db = DataManager(this)

        //DUMMY DATA
//        db.addCard("Work")
//        db.addCard("School")
//        db.addTask(1,"Meeting","Meeting with randy",1654056000000)
//        db.addTask(2,"Final Exam","CSC 302",1654056000000)

        //Initializing the view pager adapter
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        //assigning the layout ViewPager2 to viewpager with the id of viewpager
        val viewpager = findViewById<ViewPager2>(R.id.viewpager)
        viewpager.adapter = adapter


        //Cards must be read from the database
        //Before accessing any data, the database must be read to set local variables
        db.readCards()
        db.readTask()
        //the cards are set for the viewpager
        //Viewpager has its own local storage for card objects
        adapter.setCards(db.getCards())

        //Debugging
        var nm = db.getCards()[1].getTasks()[0].getDesc()
        Log.d(TAG,"$nm")
    }
}
