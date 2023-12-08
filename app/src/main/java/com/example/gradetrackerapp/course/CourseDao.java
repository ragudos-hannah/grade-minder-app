package com.example.gradetrackerapp.course;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    void insert(Course... courses);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses")
    List<Course> getAllCourses();
} // end of CourseDao interface
