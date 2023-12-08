package com.example.gradetrackerapp.database;

import androidx.room.TypeConverter;

import com.example.gradetrackerapp.course.Course;
import com.example.gradetrackerapp.course.GradeCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class GradeCategoryConverter {
    @TypeConverter
    public static String fromGradeCategoryList(List<GradeCategory> gradeCategories) {
        return new Gson().toJson(gradeCategories);
    }

    @TypeConverter
    public static List<GradeCategory> toGradeCategoryList(String value) {
        return new Gson().fromJson(value, new TypeToken<List<GradeCategory>>() {}.getType());
    }
} // end of GradeCategoryConverter class
