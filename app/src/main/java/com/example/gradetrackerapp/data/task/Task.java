package com.example.gradetrackerapp.data.task;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int taskId;

    public int termId;
    public String taskName;
    public int score;
    public int totalScore;
} // end of Task class
