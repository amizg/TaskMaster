package com.example.taskapp
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.example.taskapp.Fragments.CardFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//For debugging Log.d(TAG,"")
private val TAG: String = MainActivity::class.java.simpleName //Debugging tag

class MainActivity : AppCompatActivity() {

    // refresh function to add fragment changes
    companion object{
        lateinit var adapter: ViewPagerAdapter
        lateinit var viewpager:ViewPager2
        lateinit var dm:DataManager
        lateinit var alertBuilder: AlertDialog.Builder

        //Refresh viewpager after adding a card
        fun refresh(cards:ArrayList<Card>){
            dm.readCards()
            val pos = dm.getCards().size
            adapter.notifyItemInserted(pos)

            viewpager.currentItem = pos+1
            viewpager.currentItem = pos
        }
        //Refresh viewpager after Editing a card.
        fun editCardRefresh(cards: ArrayList<Card>){
            dm.readCards()
        }
        //Refresh viewpager after deleting a card.
        fun deleteCardRefresh(cards: ArrayList<Card>){
            dm.readCards()
            val pos = dm.getCards().size
            adapter.notifyItemRemoved(0)

            //This is dumb and bad, but it is the only way
            //To refresh the View Pager after a deletion
            //using it the way we are.
            viewpager.currentItem = pos - 5
            viewpager.currentItem = 0
        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            return format.format(date)
        }

        fun convertDateToLong(date: String): Long {
            val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
            return df.parse(date).time
        }

        fun currentTimeToLong(): Long {
            return System.currentTimeMillis()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaring and setting up the database
        dm = DataManager(this)
        alertBuilder = AlertDialog.Builder(this)

        //DUMMY DATA
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
        //dm.readTask()


        //Debugging
        //var nm = db.getCards()[1].getTasks()[0].getDesc()
        //Log.d(TAG,"$nm")

    }

}
