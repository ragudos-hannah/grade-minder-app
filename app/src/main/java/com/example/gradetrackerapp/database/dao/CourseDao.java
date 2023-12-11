package com.example.gradetrackerapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Term;

import java.util.Arrays;
import java.util.List;

@Dao
public interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    void insertCourse(Course course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCourseAndGetId(Course course);

    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    LiveData<Course> getCourseById(int courseId);

    @Query("SELECT * FROM courses")
    LiveData<List<Course>> getAllCourses();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTerms(List<Term> terms);

    @Transaction
    @Query("SELECT * FROM terms WHERE courseId = :courseId")
    LiveData<List<Term>> getTermsForCourse(int courseId);
} // end of CourseDao interface
