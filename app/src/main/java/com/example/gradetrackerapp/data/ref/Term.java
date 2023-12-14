package com.example.gradetrackerapp.data.ref;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Relation;
import androidx.room.Transaction;

import java.util.List;

@Entity(tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    public int termId;

    public int courseId; // Foreign key reference to Course

    public String termName;
    public int termGrade;
    public int targetGrade;
    public boolean examDone;

    @Embedded
    public Exam exam = new Exam();

    public Term(int courseId, String termName) {
        this.courseId = courseId;
        this.termName = termName;
        examDone = false;
        targetGrade = 75;
        termGrade = 0;
    } // end of constructor
} // end of Term class
