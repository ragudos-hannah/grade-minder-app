package com.example.gradetrackerapp.data.category;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gradetrackerapp.data.term.Term;

import java.util.List;

public class CategoryWithTerms {
    @Embedded public Category category;

    @Relation(parentColumn = "categoryId", entityColumn = "categoryId")
    public List<Term> termList;
} // end of CategoryWithTerms class
