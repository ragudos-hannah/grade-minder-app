package com.example.gradetrackerapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.adapter.CourseAdapter;
import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.view_model.HomepageViewModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {
    private HomepageViewModel homepageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // initialize view model
        homepageViewModel = new ViewModelProvider(this).get(HomepageViewModel.class);

        // initialize the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.navigation_drawer);
        }

        // observe changes in the list of courses
        homepageViewModel.getCourses().observe(this, this::updateRecyclerView);

        // set name in the homepage
        TextView nameTextView = findViewById(R.id.nameTextView);
        String name = readInfo();
        nameTextView.setText(name);

        // set name in the side nav header
        TextView nameInformationTextView = findViewById(R.id.nameInformation);
        nameInformationTextView.setText(name);

        // add an event for the addCourseButton
        Button addCourseButton = findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(v -> showAddCourseDialog());
    } // end of onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateRecyclerView(List<Course> courses) {
        CourseAdapter courseAdapter = new CourseAdapter(getApplicationContext());
        courseAdapter.setCourses(courses);

        courseAdapter.setOnItemClickListener(course -> {
            Intent intent = new Intent(HomepageActivity.this, GradeActivity.class);
            intent.putExtra("courseId", course.courseId);
            startActivity(intent);
        });

        courseAdapter.setOnEditClickListener(this::showCourseInfoDialog);

        RecyclerView recyclerView = findViewById(R.id.coursesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(courseAdapter);
    } // end of updateRecyclerView

    private void showAddCourseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Course")
                .setMessage("Supply the following information");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_course, null);
        builder.setView(view);

        EditText courseCodeInput = view.findViewById(R.id.courseCodeInput);
        EditText courseNameInput = view.findViewById(R.id.courseNameInput);
        EditText professorInput = view.findViewById(R.id.professorInput);

        builder.setPositiveButton("Add", (dialogInterface, id) -> {
            String courseCode = courseCodeInput.getText().toString().trim();
            String courseName = courseNameInput.getText().toString().trim();
            String courseInstructor = professorInput.getText().toString().trim();

            if (!TextUtils.isEmpty(courseCode) && !TextUtils.isEmpty(courseName) && !TextUtils.isEmpty(courseInstructor)) {
                Course newCourse = new Course();
                newCourse.courseCode = courseCode;
                newCourse.courseName = courseName;
                newCourse.courseInstructor = courseInstructor;

                // Insert the new course using the ViewModel
                homepageViewModel.insertCourse(newCourse);
            } else {
                Toast.makeText(HomepageActivity.this, "Invalid input. Course not added.", Toast.LENGTH_SHORT).show();
            }
            dialogInterface.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    } // end of showAddCourseDialog

    private void showCourseInfoDialog(Course existingCourse) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_course, null);

        builder.setView(dialogView);

        EditText courseNameEditText = dialogView.findViewById(R.id.courseNameEditText);
        EditText courseCodeEditText = dialogView.findViewById(R.id.courseCodeEditText);
        EditText courseInstructorEditText = dialogView.findViewById(R.id.courseInstructorEditText);
        Button editButton = dialogView.findViewById(R.id.editCourseButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        ImageButton deleteButton = dialogView.findViewById(R.id.deleteCourseButton);

        if (existingCourse != null) {
            courseNameEditText.setText(existingCourse.courseName);
            courseCodeEditText.setText(existingCourse.courseCode);
            courseInstructorEditText.setText(existingCourse.courseInstructor);
        }

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        editButton.setOnClickListener(view -> {
            String courseName = courseNameEditText.getText().toString().trim();
            String courseCode = courseCodeEditText.getText().toString().trim();
            String courseInstructor = courseInstructorEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(courseName) && !TextUtils.isEmpty(courseCode) && !TextUtils.isEmpty(courseInstructor)) {
                existingCourse.courseName = courseName;
                existingCourse.courseCode = courseCode;
                existingCourse.courseInstructor = courseInstructor;

                homepageViewModel.updateCourse(existingCourse);
            } else {
                Toast.makeText(this, "Invalid input. Unable to edit course", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        deleteButton.setOnClickListener(view -> {
            homepageViewModel.deleteCourse(existingCourse);
            dialog.dismiss();
        });

        dialog.show();
    } // end of showCourseInfoDialog

    private String readInfo() {
        File file = new File(getFilesDir(), "register_data/data.txt");
        StringBuilder stringBuilder = new StringBuilder();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return stringBuilder.toString();
    } // end of readInfo
} // end of HomepageActivity class
