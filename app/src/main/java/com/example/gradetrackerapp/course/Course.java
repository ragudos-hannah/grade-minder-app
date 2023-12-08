package com.example.gradetrackerapp.course;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import com.example.gradetrackerapp.database.GradeCategoryConverter;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String courseCode;
    private String courseName;
    private String professor;

    @TypeConverters({GradeCategoryConverter.class})
    private List<GradeCategory> gradeCategories;

    public Course(String courseCode, String courseName, String professor) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.professor = professor;
        this.gradeCategories = new ArrayList<>();
    } // end of constructor

    public int getId() {
        return id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getProfessor() {
        return professor;
    }

    public List<GradeCategory> getGradeCategories() {
        return gradeCategories;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setGradeCategories(List<GradeCategory> gradeCategories) {
        this.gradeCategories = gradeCategories;
    }

    public void addGradeCategory(GradeCategory category) {
        gradeCategories.add(category);
    }
} // end of Course class
