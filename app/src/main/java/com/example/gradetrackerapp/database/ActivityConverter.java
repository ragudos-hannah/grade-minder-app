package com.example.gradetrackerapp.database;

import androidx.room.TypeConverter;

import com.example.gradetrackerapp.course.Activity;
import com.example.gradetrackerapp.course.Course;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ActivityConverter {
    @TypeConverter
    public static String fromActivityList(List<Activity> activities) {
        return new Gson().toJson(activities);
    }

    @TypeConverter
    public static List<Activity> toActivityList(String value) {
        return new Gson().fromJson(value, new TypeToken<List<Activity>>() {}.getType());
    }
} // end of ActivityConverter class
