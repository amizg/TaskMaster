package com.example.taskapp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.taskapp.Fragments.CardFragment
import com.example.taskapp.Fragments.HomeFragment
import java.util.ArrayList

class ViewPagerAdapter(fragmentManager: FragmentManager?, lifecycle: Lifecycle?) :
    FragmentStateAdapter(
        fragmentManager!!, lifecycle!!
    ) {
    private var cards = ArrayList<Card>()
    override fun createFragment(position: Int): Fragment {
        return if (position > 0) {
            CardFragment(cards[position - 1].getName())
        } else {
            HomeFragment()
        }
    }

    override fun getItemCount(): Int {
        return cards.size + 1
    }

    fun setCards(cards: ArrayList<Card>) {
        this.cards = cards
    }
}