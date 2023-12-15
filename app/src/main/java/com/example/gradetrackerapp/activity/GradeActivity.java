package com.example.gradetrackerapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.adapter.CourseAdapter;
import com.example.gradetrackerapp.adapter.GradePagerAdapter;
import com.example.gradetrackerapp.adapter.ZoomOutPageTransformer;
import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.view_model.GradeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class GradeActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private GradeViewModel gradeViewModel;
    private GradePagerAdapter gradePagerAdapter;
    private TabLayout tabLayout;
    private Course course;

    /* Components */
    private TextView classStandingWeightTextView, examinationWeightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        classStandingWeightTextView = findViewById(R.id.classStandingWeightLabel);
        examinationWeightTextView = findViewById(R.id.examinationWeightLabel);

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

                TextView courseHeading = findViewById(R.id.courseHeading);
                String courseName = course.courseName;
                courseHeading.setText(courseName);

                classStandingWeightTextView.setText(String.valueOf(course.classStandingWeight) + "%");
                examinationWeightTextView.setText(String.valueOf(course.examWeight) + "%");

                ImageButton button = findViewById(R.id.editCourseWeightButton);
                button.setOnClickListener(v -> showEditCourseWeightDialog(course));

                gradePagerAdapter.setCourse(course);
            });

            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    tabLayout.setScrollPosition(position, 0, true);
                }
            });

            tabLayout = findViewById(R.id.viewPagerTabs);
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

    private void showEditCourseWeightDialog(Course existingCourse) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_course_weight, null);

        builder.setView(dialogView);

        EditText classStandingEditText = dialogView.findViewById(R.id.classStandingWeightEditText);
        EditText examinationEditText = dialogView.findViewById(R.id.examinationWeightEditText);
        MaterialButton editButton = dialogView.findViewById(R.id.editCourseWeightButton);
        MaterialButton cancelButton = dialogView.findViewById(R.id.cancelButton);

        if (existingCourse != null) {
            classStandingEditText.setText(String.valueOf(existingCourse.classStandingWeight));
            examinationEditText.setText(String.valueOf(existingCourse.examWeight));
        }

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        editButton.setOnClickListener(view -> {
            String classStanding = classStandingEditText.getText().toString().trim();
            String examination = examinationEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(classStanding) && !TextUtils.isEmpty(examination)) {
                int classStandingWeight = Integer.parseInt(classStanding);
                int examinationWeight = Integer.parseInt(examination);

                if (classStandingWeight + examinationWeight == 100) {
                    classStandingWeightTextView.setText(String.valueOf(existingCourse.classStandingWeight) + "%");
                    examinationWeightTextView.setText(String.valueOf(existingCourse.examWeight) + "%");

                    existingCourse.classStandingWeight = classStandingWeight;
                    existingCourse.examWeight = examinationWeight;

                    gradeViewModel.updateCourse(existingCourse);
                } else {
                    Toast.makeText(this, "The total of the two weights should equal to 100", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid input. Unable to edit weights", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        dialog.show();
    } // end of showEditCourseWeight
} // end of GradeActivity class
