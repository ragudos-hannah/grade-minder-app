package com.example.gradetrackerapp.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.data.course.Course;
import com.example.gradetrackerapp.data.course.CourseAdapter;
import com.example.gradetrackerapp.grades.GradeActivity;

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

        // observe changes in the list of courses
        homepageViewModel.getCoursesLiveData().observe(this, courses -> {
            updateRecyclerView(courses);
        });

        // set name in the homepage
        TextView nameTextView = findViewById(R.id.nameTextView);
        String name = readInfo();
        nameTextView.setText(name);

        TextView nameInformationTextView = findViewById(R.id.nameInformation);
        nameInformationTextView.setText(name);

        Button addCourseButton = findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(v -> showAddCourseDialog());
    } // end of onCreate

    private void updateRecyclerView(List<Course> courses) {
        CourseAdapter courseAdapter = new CourseAdapter(getApplicationContext());
        courseAdapter.setCoursesList(courses);

        courseAdapter.setOnItemClickListener(course -> {
            Intent intent = new Intent(HomepageActivity.this, GradeActivity.class);
            intent.putExtra("courseId", course.courseId);
            startActivity(intent);
        });

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
