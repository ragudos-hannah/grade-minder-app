package com.example.gradetrackerapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Exam;
import com.example.gradetrackerapp.data.ref.Task;
import com.example.gradetrackerapp.data.ref.Term;
import com.example.gradetrackerapp.database.dao.CourseDao;
import com.example.gradetrackerapp.database.dao.TaskDao;
import com.example.gradetrackerapp.database.dao.TermDao;

import java.util.Arrays;
import java.util.List;

@Database(entities = {Course.class, Term.class, Task.class, Exam.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();
    public abstract TermDao termDao();
    public abstract TaskDao taskDao();

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "course-database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    } // end of getInstance

    public void insertCourse(final Course course) {
        if (INSTANCE != null) {
            INSTANCE.runInTransaction(new Runnable() {
                @Override
                public void run() {
                    int courseId = (int) INSTANCE.courseDao().insertCourseAndGetId(course);

                    List<Term> defaultTerms = Arrays.asList(
                            new Term(courseId, "Prelims"),
                            new Term(courseId, "Midterms"),
                            new Term(courseId, "Finals")
                    );
                    courseDao().insertTerms(defaultTerms);
                }
            });
        }
    } // end of insertCourse

    public void insertTask(final Task task) {
        if (INSTANCE != null) {
            INSTANCE.runInTransaction(new Runnable() {
                @Override
                public void run() {
                    taskDao().insertTask(task);
                }
            });
        }
    } // end of insertTask
} // end of AppDatabase
