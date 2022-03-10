package com.example.taskapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.taskapp.Fragments.CardFragment;
import com.example.taskapp.Fragments.HomeFragment;
import com.example.taskapp.Fragments.TestFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle){
        super(fragmentManager, lifecycle);
    }
    private ArrayList<Fragment> fragments;


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch(position)
        {
            case 0:
                return new HomeFragment();
            case 1:
                return new CardFragment();
            case 2:
                return new TestFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
