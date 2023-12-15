package com.example.gradetrackerapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gradetrackerapp.callback.OnEditClickListener;
import com.example.gradetrackerapp.callback.OnItemClickListener;
import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Term;
import com.example.gradetrackerapp.fragment.OverallFragment;
import com.example.gradetrackerapp.fragment.TermFragment;

import java.util.List;

public class GradePagerAdapter extends FragmentStateAdapter {
    private List<Term> terms;
    private Course course;

    public GradePagerAdapter(FragmentActivity fragmentActivity, List<Term> terms, Course course) {
        super(fragmentActivity);
        this.terms = terms;
        this.course = course;
    } // end of constructor

    public void setTerms(List<Term> terms) {
        this.terms = terms;
        notifyDataSetChanged();
    }

    public void setCourse(Course course) {
        this.course = course;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TermFragment(terms.get(0), course);
            case 1:
                return new TermFragment(terms.get(1), course);
            case 2:
                return new TermFragment(terms.get(2), course);
            case 3:
                return new OverallFragment(course);
            default:
                throw new IllegalArgumentException("Invalid position");
        }
    } // end of getItem

    @Override
    public int getItemCount() {
        return 4;
    } // end of getCount

    @Nullable
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PRELIMS";
            case 1:
                return "MIDTERMS";
            case 2:
                return "FINALS";
            case 3:
                return "OVERALL";
            default:
                return null;
        }
    }
} // end of GradePagerAdapter class
