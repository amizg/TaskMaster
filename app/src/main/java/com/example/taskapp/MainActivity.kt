package com.example.taskapp
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

//For debugging Log.d(TAG,"")
private val TAG: String = MainActivity::class.java.simpleName //Debugging tag

class MainActivity : AppCompatActivity() {

    // refresh function to add fragment changes
    companion object{
        lateinit var adapter: ViewPagerAdapter
        lateinit var viewpager:ViewPager2
        lateinit var dm:DataManager

        fun refresh(cards:ArrayList<Card>){
            dm.readCards()
            adapter.setCards(cards)
            val pos = dm.getCards().size
            adapter.notifyItemInserted(pos)
            viewpager.currentItem = pos+1
            viewpager.currentItem = pos
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaring and setting up the database
        dm = DataManager(this)

        //DUMMY DATA
//        db.addCard("Work")
//        db.addCard("School")
//        db.addTask(1,"Meeting","Meeting with randy",1654056000000)
//        db.addTask(2,"Final Exam","CSC 302",1654056000000)

        //Initializing the view pager adapter
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        //assigning the layout ViewPager2 to viewpager with the id of viewpager
        viewpager = findViewById(R.id.viewpager)
        viewpager.adapter = adapter


        //Cards must be read from the database
        //Before accessing any data, the database must be read to set local variables
        dm.readCards()
        dm.readTask()
        //the cards are set for the viewpager
        //Viewpager has its own local storage for card objects
        adapter.setCards(dm.getCards())

        //Debugging
        //var nm = db.getCards()[1].getTasks()[0].getDesc()
        //Log.d(TAG,"$nm")
    }
}
