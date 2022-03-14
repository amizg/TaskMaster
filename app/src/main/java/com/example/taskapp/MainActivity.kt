package com.example.taskapp
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

private val TAG: String = MainActivity::class.java.simpleName //Debugging tag
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaring and setting up the database
        val db:DataManager = DataManager(this)

        //Initializing the view pager adapter
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        //assigning the layout ViewPager2 to viewpager with the id of viewpager
        val viewpager = findViewById<ViewPager2>(R.id.viewpager)
        viewpager.adapter = adapter

        //Cards must be read from the database
        //Before accessing any data, the database must be read to set local variables
        db.readCards()
        //the cards are set for the viewpager
        //Viewpager has its own local storage for card objects
        adapter.setCards(db.getCards())
    }
}
