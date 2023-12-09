package com.example.gradetrackerapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gradetrackerapp.data.category.Category;
import com.example.gradetrackerapp.data.course.Course;
import com.example.gradetrackerapp.data.task.Task;
import com.example.gradetrackerapp.data.term.Term;
import com.example.gradetrackerapp.database.dao.CategoryDao;
import com.example.gradetrackerapp.database.dao.CourseDao;
import com.example.gradetrackerapp.database.dao.TaskDao;
import com.example.gradetrackerapp.database.dao.TermDao;

import java.util.Arrays;
import java.util.List;

@Database(entities = {Course.class, Category.class, Term.class, Task.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();
    public abstract CategoryDao categoryDao();
    public abstract TermDao termDao();
    public abstract TaskDao taskDao();

    private static final List<Term> DEFAULT_TERMS = Arrays.asList(
            new Term(0, "Prelims"),
            new Term(0, "Midterms"),
            new Term(0, "Finals")
    );

    public static void initializeDefaultTerms(Context context, int courseId) {
        AppDatabase database = getInstance(context);
        TermDao termDao = database.termDao();
        for (Term term : DEFAULT_TERMS) {
            term.categoryId = courseId;
            termDao.insertTerm(term);
        }
    }

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "course-database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    } // end of getInstance
} // end of AppDatabase class
