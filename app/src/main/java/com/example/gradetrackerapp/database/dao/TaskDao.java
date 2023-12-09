package com.example.gradetrackerapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gradetrackerapp.data.task.Task;

import java.util.List;


@Dao
public interface TaskDao {
    @Insert
    void insertTerm(Task task);

    @Query("SELECT * FROM task_table WHERE termId = :termId")
    LiveData<List<Task>> getTasksByTerm(int termId);
} // end of TaskDao interface
