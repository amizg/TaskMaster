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
    private var cards = ArrayList<Card>()
    override fun createFragment(position: Int): Fragment {
        return when {
            position > 0 && position <= (cards.size) -> {
                CardFragment(cards[position - 1].getName())
            }
            position > 0 && position >= (cards.size) -> {
                AddCardFragment()
            }
            else -> {
                HomeFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return cards.size + 2
    }

    fun setCards(cards: ArrayList<Card>) {
        this.cards = cards
    }

}