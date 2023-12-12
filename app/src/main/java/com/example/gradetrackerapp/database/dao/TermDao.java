package com.example.gradetrackerapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Task;
import com.example.gradetrackerapp.data.ref.Term;

import java.util.List;

@Dao
public interface TermDao {

    @Update
    void updateTerm(Term term);

    @Transaction
    @Query("SELECT courses.* FROM courses INNER JOIN terms ON courses.courseId = terms.courseId WHERE terms.termId = :termId")
    LiveData<Course> getCourseForTerm(int termId);

    @Transaction
    @Query("SELECT * FROM tasks WHERE termId = :termId")
    LiveData<List<Task>> getTasksForTerm(int termId);
} // end of TermDao interface
