package com.example.gradetrackerapp.data.ref;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int taskId;

    public int termId; // Foreign key reference to Term

    public String taskName;
    public int score;
    public int totalScore;

    public Task() {
        score = 0;
        totalScore = 0;
    } // end of constructor
} // end of Task class
