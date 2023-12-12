package com.example.gradetrackerapp.callback;

import androidx.viewpager2.widget.ViewPager2;

public class OnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
    private ViewPager2 viewPager;
    private boolean isSwipeEnabled = true;

    public OnPageChangeCallback(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);

        // Check if swipe is enabled before setting the current item
        if (isSwipeEnabled) {
            viewPager.setCurrentItem(position);
        }
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        isSwipeEnabled = swipeEnabled;
    }
}
