package com.example.gradetrackerapp.grades;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class GradePagerAdapter extends FragmentPagerAdapter {

    public GradePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    } // end of constructor

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PrelimsFragment();
            case 1:
                return new MidtermsFragment();
            case 2:
                return new FinalsFragment();
            default:
                throw new IllegalArgumentException("Invalid position");
        }
    } // end of getItem

    @Override
    public int getCount() {
        return 3;
    } // end of getCount
} // end of GradePagerAdapter class
