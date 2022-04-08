package com.example.taskapp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.taskapp.Fragments.CardFragment
import com.example.taskapp.Fragments.HomeFragment
import kotlin.collections.ArrayList

class ViewPagerAdapter(fm: FragmentManager?, lifecycle: Lifecycle?) :
    FragmentStateAdapter(fm!!, lifecycle!!) { //, ViewPager2.PageTransformer (add this to args for animation)

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