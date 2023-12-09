package com.example.gradetrackerapp.data.course;

import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "course_table")
public class Course {
    @PrimaryKey(autoGenerate = true)
    public int courseId;

    public String courseCode;
    public String courseName;
    public String courseInstructor;
} // end of Course class
