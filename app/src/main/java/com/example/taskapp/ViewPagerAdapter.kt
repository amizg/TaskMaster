package com.example.taskapp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.taskapp.Fragments.AddCardFragment
import com.example.taskapp.Fragments.CardFragment
import com.example.taskapp.Fragments.HomeFragment
import com.example.taskapp.Fragments.RoutinesFragment
import kotlin.collections.ArrayList

class ViewPagerAdapter(fm: FragmentManager?, lifecycle: Lifecycle?) :
    FragmentStateAdapter(fm!!, lifecycle!!) { //, ViewPager2.PageTransformer (add this to args for animation)

    // chooses which fragment should be displayed based on position of current screen
    override fun createFragment(position: Int): Fragment {

        // array of all card objects
        val cards: ArrayList<Card> = MainActivity.dm.readCards()

        return when {
            // loads CardFragment from current position in card array
            position > 0 && position <= (cards.size) -> {
                CardFragment(
                    cards[position - 1].getId(),
                    cards[position - 1].getName(),
                    cards[position - 1].getTasks())
            }
            // loads AddCardFragment at end of ViewPager
            position > 0 && position == (cards.size+1)-> {
                AddCardFragment()
            }
            // loads RoutinesFragment at end of ViewPager
            position > 0 && position > (cards.size+1) -> {
                RoutinesFragment()
            }
            else -> {
                // in position 0, loads the home screen
                HomeFragment()
            }
        }
    }

    // returns size to ViewPager(Card array size + AddCard, Home, & Routines)
    override fun getItemCount(): Int {
        return MainActivity.dm.readCards().size + 3
    }

    //Animations for scrolling, still need to work on this - Adam
//    override fun transformPage(page: View, position: Float) {
//        val scale = if (position < 0.0f) position + 1.0f else Math.abs(1.0f - position)
//        page.scaleX = scale
//        page.scaleY = scale
//        page.pivotX = page.width.toFloat() * 0.5f
//        page.pivotY = page.height.toFloat() * 0.5f
//        page.alpha = if (position >= -1.0f && position <= 1.0f) 1.0f - (scale - 1.0f) else 0.0f
//    }

}