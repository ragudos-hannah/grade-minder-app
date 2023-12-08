package com.example.gradetrackerapp.database;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.gradetrackerapp.course.Course;
import com.example.gradetrackerapp.course.Term;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class TermConverter {
    @TypeConverter
    public static String fromTermList(List<Term> terms) {
        return new Gson().toJson(terms);
    }

    @TypeConverter
    public static List<Term> toTermList(String value) {
        return new Gson().fromJson(value, new TypeToken<List<Term>>() {}.getType());
    }
} // end of TermConverter class
