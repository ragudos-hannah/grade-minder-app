package com.example.gradetrackerapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.gradetrackerapp.data.course.Course;
import com.example.gradetrackerapp.data.course.CourseWithCategories;
import com.example.gradetrackerapp.data.term.Term;

import java.util.Arrays;
import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    @Transaction
    default void insertCourse(Course course) {
        int courseId = (int) insertCourseAndGetId(course);

        // Insert default terms associated with the course
        List<Term> defaultTerms = Arrays.asList(
                new Term(courseId, "Prelims"),
                new Term(courseId, "Midterms"),
                new Term(courseId, "Finals")
        );
        for (Term term : defaultTerms) {
            insertTerm(term);
        }
    }

    @Insert
    long insertCourseAndGetId(Course course);

    @Insert
    void insertTerm(Term term);

    @Query("SELECT * FROM course_table WHERE courseId = :courseId")
    LiveData<Course> getCourseById(int courseId);

    @Query("SELECT * FROM course_table")
    LiveData<List<Course>> getAllCourses();

    @Transaction
    @Query("SELECT * FROM course_table")
    LiveData<List<CourseWithCategories>> getCoursesWithCategories();
} // end of CourseDao interface
