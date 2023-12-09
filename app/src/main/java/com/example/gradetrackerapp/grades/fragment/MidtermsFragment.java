package com.example.gradetrackerapp.grades.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.data.category.Category;
import com.example.gradetrackerapp.data.course.Course;

import java.util.List;

public class MidtermsFragment extends Fragment {
    private List<Category> categories;
    private Course course;

    public MidtermsFragment(List<Category> categories, Course course) {
        this.categories = categories;
        this.course = course;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);

        TextView heading = view.findViewById(R.id.heading);
        String newHeading = "Midterms";
        heading.setText(newHeading);

        TextView courseNameTest = view.findViewById(R.id.courseNameTest);
        String courseNameTestHeading = course.courseName;
        courseNameTest.setText(courseNameTestHeading);


        return view;
    } // end of onCreateView
} // end of MidtermsFragment class
