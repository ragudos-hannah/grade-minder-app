package com.example.gradetrackerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.adapter.GradePagerAdapter;
import com.example.gradetrackerapp.adapter.ZoomOutPageTransformer;
import com.example.gradetrackerapp.callback.OnPageChangeCallback;
import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Term;
import com.example.gradetrackerapp.view_model.GradeViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class GradeActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private GradeViewModel gradeViewModel;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        Intent intent = getIntent();
        if (intent.hasExtra("courseId")) {
            int courseId = intent.getIntExtra("courseId", -1);

            gradeViewModel = new GradeViewModel(this.getApplication());
            gradeViewModel.getCourseById(courseId).observe(this, course -> {
                this.course = course;

                TextView courseHeading = findViewById(R.id.courseLBL);
                String courseName = course.courseName;
                courseHeading.setText(courseName);
            });
            gradeViewModel.getTerms(courseId).observe(this, this::setupViewPager);

            /*
            // Add OnTabSelectedListener to handle tab click events
            TabLayout tabLayout = findViewById(R.id.viewPagerLBL);
            viewPager = findViewById(R.id.viewPager);

            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    tabLayout.setScrollPosition(position, 0, true);
                }
            });

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    // Handle tab unselected
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    // Handle tab reselected
                }
            });

             */
        }
    } // end of onCreate

    private void setupViewPager(List<Term> terms) {
        //TabLayout tabLayout = findViewById(R.id.viewPagerLBL);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        GradePagerAdapter gradePagerAdapter = new GradePagerAdapter(this, terms, course);
        OnPageChangeCallback onPageChangeCallback = new OnPageChangeCallback(viewPager);

        //tabLayout.setupWithViewPager(viewPager);

        viewPager.registerOnPageChangeCallback(onPageChangeCallback);
        viewPager.setAdapter(gradePagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        onPageChangeCallback.setSwipeEnabled(true);
    } // end of setupViewPager
} // end of GradeActivity class
