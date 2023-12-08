package com.example.gradetrackerapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.gradetrackerapp.course.Course;
import com.example.gradetrackerapp.course.CourseDao;

@Database(entities = {Course.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();
    private static AppDatabase instance;
    public static AppDatabase getDbInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Course Database")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    } // end of getDbInstance
} // end of AppDatabase class
