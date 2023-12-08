package com.example.gradetrackerapp.course;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.gradetrackerapp.database.TermConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class GradeCategory {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String categoryName;

    @TypeConverters({TermConverter.class})
    private List<Term> terms;

    public GradeCategory(String categoryName) {
        this.categoryName = categoryName;
        this.terms = new ArrayList<>();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setTerms() {
        terms.add(new Term("Prelims"));
        terms.add(new Term("Midterms"));
        terms.add(new Term("Finals"));
    }
} // end of GradeCategory class
