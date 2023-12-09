package com.example.gradetrackerapp.data.category;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "category_table")
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int categoryId;

    public int courseId;
    public String categoryName;
    public int categoryWeight;
} // end of Category class
