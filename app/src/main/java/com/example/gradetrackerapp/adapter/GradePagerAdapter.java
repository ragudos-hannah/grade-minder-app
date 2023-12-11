package com.example.gradetrackerapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Term;
import com.example.gradetrackerapp.fragment.FinalsFragment;
import com.example.gradetrackerapp.fragment.MidtermsFragment;
import com.example.gradetrackerapp.fragment.PrelimsFragment;

import java.util.List;

public class GradePagerAdapter extends FragmentPagerAdapter {
    private List<Term> terms;
    private Course course;

    public GradePagerAdapter(FragmentManager fragmentManager, List<Term> terms, Course course) {
        super(fragmentManager);
        this.terms = terms;
        this.course = course;
    } // end of constructor

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PrelimsFragment(terms.get(0), course);
            case 1:
                return new MidtermsFragment(terms.get(1), course);
            case 2:
                return new FinalsFragment(terms.get(2), course);
            case 3:
                return new PrelimsFragment(terms.get(0), course);
            default:
                throw new IllegalArgumentException("Invalid position");
        }
    } // end of getItem

    @Override
    public int getCount() {
        return 4;
    } // end of getCount
} // end of GradePagerAdapter class
