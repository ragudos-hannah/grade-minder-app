package com.example.gradetrackerapp.data.course;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gradetrackerapp.data.category.Category;

import java.util.List;

public class CourseWithCategories {
    @Embedded public Course course;

    @Relation(parentColumn = "courseId", entityColumn = "courseId")
    public List<Category> categoryList;
} // end of CourseWithCategories class
