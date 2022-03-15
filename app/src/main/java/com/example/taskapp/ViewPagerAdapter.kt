package com.example.taskapp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.taskapp.Fragments.CardFragment
import com.example.taskapp.Fragments.HomeFragment
import java.util.*
import kotlin.collections.ArrayList

class ViewPagerAdapter(fm: FragmentManager?, lifecycle: Lifecycle?) :
    FragmentStateAdapter(
        fm!!, lifecycle!!
    ) {
    private var currPos: Int = 0
    //private var cards = ArrayList<Card>()

    override fun createFragment(position: Int): Fragment {

        return when {
            position > 0 && position <= (MainActivity.dm.getCards().size) -> {
                currPos = position
                CardFragment(MainActivity.dm.getCards()[position - 1].getId(), MainActivity.dm.getCards()[position - 1].getName())
            }
            position > 0 && position > (MainActivity.dm.getCards().size) -> {
                currPos = position
                AddCardFragment()
            }
            else -> {
                currPos = position
                HomeFragment()
            }
        }
    }

    fun getPosition(): Int{
        return currPos
    }

    override fun getItemCount(): Int {
        return MainActivity.dm.getCards().size + 2
    }

}