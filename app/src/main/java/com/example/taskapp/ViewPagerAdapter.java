package com.example.taskapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.taskapp.Fragments.HomeFragment;
import com.example.taskapp.Fragments.CardFragment;

import java.util.ArrayList;
import java.util.Vector;
import com.example.taskapp.MainActivity.*;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private int size = 1;
    private ArrayList<Card> cards = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle){
        super(fragmentManager, lifecycle);
    }
    private ArrayList<Fragment> fragments;

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){ return new HomeFragment(); }
        else if (position > 0) {
            return new CardFragment(cards.get(position-1).getName()); }
        else {}
        return null;
    }

    @Override
    public int getItemCount() {
        return this.cards.size()+1;
    }
    public void setCards(ArrayList<Card> cards){
        this.cards = cards;
    }
}
