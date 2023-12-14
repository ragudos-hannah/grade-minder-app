package com.example.gradetrackerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.adapter.GradePagerAdapter;
import com.example.gradetrackerapp.adapter.ZoomOutPageTransformer;
import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.view_model.GradeViewModel;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class GradeActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private GradeViewModel gradeViewModel;
    private GradePagerAdapter gradePagerAdapter;
    private TabLayout tabLayout;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        Intent intent = getIntent();
        if (intent.hasExtra("courseId")) {
            int courseId = intent.getIntExtra("courseId", -1);

            viewPager = findViewById(R.id.viewPager);
            gradePagerAdapter = new GradePagerAdapter(this, new ArrayList<>(), course);
            viewPager.setAdapter(gradePagerAdapter);
            viewPager.setPageTransformer(new ZoomOutPageTransformer());

            gradeViewModel = new GradeViewModel(this.getApplication());
            gradeViewModel.getCourseById(courseId).observe(this, course -> {
                this.course = course;

                TextView courseHeading = findViewById(R.id.courseLBL);
                String courseName = course.courseName;
                courseHeading.setText(courseName);

                gradePagerAdapter.setCourse(course);
            });

            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    tabLayout.setScrollPosition(position, 0, true);
                }
            });

            tabLayout = findViewById(R.id.viewPagerLBL);
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

            gradeViewModel.getTerms(courseId).observe(this, terms -> {
                int currentPosition = viewPager.getCurrentItem();
                gradePagerAdapter.setTerms(terms);
                gradePagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(currentPosition, false);
            });
        }
    } // end of onCreate
} // end of GradeActivity class
