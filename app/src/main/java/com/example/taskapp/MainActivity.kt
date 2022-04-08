package com.example.taskapp
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.taskapp.Fragments.RecyclerAdapter
import java.text.SimpleDateFormat
import java.util.*

//For debugging Log.d(TAG,"")
private val TAG: String = MainActivity::class.java.simpleName //Debugging tag

class MainActivity : AppCompatActivity() {

    // refresh function to add fragment changes
    companion object{
        lateinit var rcy: RecyclerAdapter
        lateinit var adapter: ViewPagerAdapter
        lateinit var viewpager:ViewPager2
        lateinit var dm:DataManager
        lateinit var fm: FragmentManager
        lateinit var alertBuilder: AlertDialog.Builder


        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("E h:mm a \n 'on' \n MM/dd/yyyy")
            return format.format(date)
        }

        fun convertDateToLong(date: String): Long {
            val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
            return df.parse(date).time
        }

        fun covertLongToSimpleTime(time: Long): String{
            val date = Date(time)
            val format = SimpleDateFormat("h:mm a")
            return format.format(date)
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
        alertBuilder = AlertDialog.Builder(this, R.style.AlertDialogTheme)

        //Initializing the view pager adapter
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        fm = supportFragmentManager
        //assigning the layout ViewPager2 to viewpager with the id of viewpager
        viewpager = findViewById(R.id.viewpager)
        viewpager.adapter = adapter

    }

}
