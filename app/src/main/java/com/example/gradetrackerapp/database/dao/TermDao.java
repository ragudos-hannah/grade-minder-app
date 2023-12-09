package com.example.gradetrackerapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.gradetrackerapp.data.term.Term;

import java.util.List;

@Dao
public interface TermDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTerm(Term term);

    @Transaction
    @Insert
    void insertDefaultTerms(List<Term> defaultTerms);

    @Query("SELECT * FROM term_table WHERE categoryId = :categoryId")
    LiveData<List<Term>> getTermsByCategory(int categoryId);
} // end of TermDao interface
