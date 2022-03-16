package com.example.taskapp
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.taskapp.Fragments.CardFragment
import com.example.taskapp.Fragments.HomeFragment
import java.util.*
import kotlin.collections.ArrayList

private val TAG: String = ViewPagerAdapter::class.java.simpleName //Debugging tag

class ViewPagerAdapter(fm: FragmentManager?, lifecycle: Lifecycle?) :
    FragmentStateAdapter(
        fm!!, lifecycle!!
    ) {
    //private var cards = ArrayList<Card>()

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "Called Create Fragment")
        Log.d(TAG, itemCount.toString())

        var cards: ArrayList<Card> = MainActivity.dm.getCards()

        return when {
            position > 0 && position <= (cards.size) -> {
                CardFragment(cards[position - 1].getId(), cards[position - 1].getName())
            }
            position > 0 && position > (cards.size) -> {
                AddCardFragment()
            }
            else -> {
                HomeFragment()
            }

        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "Called Item Count")
        return MainActivity.dm.getCards().size + 2
    }

}