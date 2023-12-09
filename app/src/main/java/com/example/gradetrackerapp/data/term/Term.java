package com.example.gradetrackerapp.data.term;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "term_table")
public class Term {
    @PrimaryKey(autoGenerate = true)
    public int termId;

    public int categoryId;
    public String termName;

    public Term(int categoryId, String termName) {
        this.categoryId = categoryId;
        this.termName = termName;
    } // end of constructor
} // end of Term class
