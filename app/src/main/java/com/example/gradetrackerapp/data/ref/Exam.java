package com.example.gradetrackerapp.data.ref;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exam {
    @PrimaryKey(autoGenerate = true)
    public int examId;

    public int score;
    public int totalScore;

    public Exam() {
        score = 100;
        totalScore = 100;
    } // end of constructor
} // end of Exam class
