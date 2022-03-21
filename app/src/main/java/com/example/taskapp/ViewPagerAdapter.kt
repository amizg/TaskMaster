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

    override fun createFragment(position: Int): Fragment {

        var cards: ArrayList<Card> = MainActivity.dm.getCards()

        return when {
            position > 0 && position <= (cards.size) -> {
                CardFragment(
                    cards[position - 1].getId(),
                    cards[position - 1].getName(),
                    cards[position - 1].getTasks())
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
        return MainActivity.dm.getCards().size + 2
    }

}