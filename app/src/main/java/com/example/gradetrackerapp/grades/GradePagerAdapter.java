package com.example.gradetrackerapp.grades;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LiveData;

import com.example.gradetrackerapp.data.category.Category;
import com.example.gradetrackerapp.data.course.Course;
import com.example.gradetrackerapp.grades.fragment.FinalsFragment;
import com.example.gradetrackerapp.grades.fragment.MidtermsFragment;
import com.example.gradetrackerapp.grades.fragment.PrelimsFragment;

import java.util.List;

public class GradePagerAdapter extends FragmentPagerAdapter {
    private List<Category> categories;
    private Course course;

    public GradePagerAdapter(FragmentManager fragmentManager, List<Category> categories, Course course) {
        super(fragmentManager);
        this.categories = categories;
        this.course = course;
    } // end of constructor

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PrelimsFragment(categories, course);
            case 1:
                return new MidtermsFragment(categories, course);
            case 2:
                return new FinalsFragment(categories, course);
            default:
                throw new IllegalArgumentException("Invalid position");
        }
    } // end of getItem

    @Override
    public int getCount() {
        return 3;
    } // end of getCount
} // end of GradePagerAdapter class
