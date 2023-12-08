package com.example.gradetrackerapp.homepage;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.course.Course;
import com.example.gradetrackerapp.grades.GradeActivity;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomepageActivity extends AppCompatActivity {
    List<Course> courses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // set name in the homepage
        TextView nameTextView = findViewById(R.id.nameTextView);
        String name = readInfo();
        nameTextView.setText(name);

        // set name info in the nav header
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View navigationHeader = navigationView.getHeaderView(0);
        TextView nameNavigationHeader = navigationHeader.findViewById(R.id.nameInformation);
        nameNavigationHeader.setText(name);


        Button addCourseButton = findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(v -> showAddCourseDialog());


        /*
        Button gradeButton = findViewById(R.id.goToGradeButton);
        gradeButton.setOnClickListener(v -> {
            Intent gradeIntent = new Intent(HomepageActivity.this, GradeActivity.class);
            startActivity(gradeIntent);
        });

         */
    } // end of onCreate

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
            String professor = professorInput.getText().toString().trim();

            if (!TextUtils.isEmpty(courseCode) && !TextUtils.isEmpty(courseName) && !TextUtils.isEmpty(professor)) {
                Course newCourse = new Course(courseCode, courseName, professor);
                courses.add(newCourse);
                updateNavigationDrawerMenu();
            } else {
                Toast.makeText(HomepageActivity.this, "Invalid input. Course not added.", Toast.LENGTH_SHORT).show();
            }
            dialogInterface.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    } // end of showAddCourseDialog

    private void updateNavigationDrawerMenu() {
        NavigationView navigationView = findViewById(R.id.navigation_view);

        Menu menu = navigationView.getMenu();
        menu.clear();

        for (Course course : courses) {
            int itemId = generateCourseMenuItemId(course);

            menu.add(R.id.group_courses, itemId, Menu.NONE, course.getCourseName());
        }
    }

    private int generateCourseMenuItemId(Course course) {
        String uniqueString = course.getCourseCode() + course.getCourseName() + course.getProfessor();
        return uniqueString.hashCode();
    } // end of generateCourseMenuItemId

    private void openGradeActivity(Course course) {
        Intent gradeIntent = new Intent(HomepageActivity.this, GradeActivity.class);
        gradeIntent.putExtra("courseCode", course.courseCode);
        gradeIntent.putExtra("courseName", course.courseName);
        gradeIntent.putExtra("professor", course.professor);
        startActivity(gradeIntent);
    } // end of openGradeActivity

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
