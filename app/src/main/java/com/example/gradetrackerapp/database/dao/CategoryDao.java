package com.example.gradetrackerapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.gradetrackerapp.data.category.Category;
import com.example.gradetrackerapp.data.category.CategoryWithTerms;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertCategory(Category category);

    @Query("SELECT * FROM category_table WHERE courseId = :courseId")
    LiveData<List<Category>> getCategoriesByCourse(int courseId);

    @Transaction
    @Query("SELECT * FROM category_table WHERE courseId = :courseId")
    LiveData<List<CategoryWithTerms>> getCategoriesWithTermsByCourse(int courseId);
} // end of CategoryDao interface
