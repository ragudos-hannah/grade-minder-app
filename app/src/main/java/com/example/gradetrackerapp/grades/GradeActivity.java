package com.example.gradetrackerapp.grades;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.data.category.Category;
import com.example.gradetrackerapp.data.course.Course;

import java.util.List;

public class GradeActivity extends AppCompatActivity {
    private GradeViewModel gradeViewModel;
    private Course courseLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        Intent intent = getIntent();
        if (intent.hasExtra("courseId")) {
            int courseId = intent.getIntExtra("courseId", -1);


            gradeViewModel = new GradeViewModel(this.getApplication(), courseId);
            gradeViewModel.getCourseById(courseId).observe(this, course -> {
                courseLiveData = course;
            });
            gradeViewModel.getCategoriesLiveData().observe(this, categories -> setupViewPager(categories));
        }
    } // end of onCreate

    private void setupViewPager(List<Category> categories) {
        ViewPager viewPager = findViewById(R.id.viewPager);
        GradePagerAdapter gradePagerAdapter = new GradePagerAdapter(getSupportFragmentManager(), categories, courseLiveData);
        viewPager.setAdapter(gradePagerAdapter);
    }
} // end of GradeActivity class
