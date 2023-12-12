package com.example.gradetrackerapp.adapter;

import android.view.View;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        // Scale the page down (between MIN_SCALE and 1)
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        float verticalMargin = pageHeight * (1 - scaleFactor) / 2;
        float horizontalMargin = pageWidth * (1 - scaleFactor) / 2;

        // Adjust the translation based on the position
        if (position < 0) {
            page.setTranslationX(horizontalMargin - verticalMargin / 2);
        } else {
            page.setTranslationX(-horizontalMargin + verticalMargin / 2);
        }

        // Scale the page
        page.setScaleX(scaleFactor);
        page.setScaleY(scaleFactor);

        // Fade the page based on position
        page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
    }
}
