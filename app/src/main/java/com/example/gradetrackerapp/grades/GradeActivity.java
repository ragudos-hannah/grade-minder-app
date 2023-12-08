package com.example.gradetrackerapp.grades;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.gradetrackerapp.R;

public class GradeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        ViewPager viewPager = findViewById(R.id.viewPager);
        GradePagerAdapter gradePagerAdapter = new GradePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(gradePagerAdapter);
    } // end of onCreate
} // end of GradeActivity class
