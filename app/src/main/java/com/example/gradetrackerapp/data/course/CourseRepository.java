package com.example.gradetrackerapp.data.course;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.gradetrackerapp.data.category.Category;
import com.example.gradetrackerapp.database.AppDatabase;
import com.example.gradetrackerapp.database.dao.CategoryDao;
import com.example.gradetrackerapp.database.dao.CourseDao;
import com.example.gradetrackerapp.database.dao.TermDao;

import java.util.List;

public class CourseRepository {
    private CourseDao courseDao;
    private TermDao termDao;
    private CategoryDao categoryDao;
    private LiveData<List<Course>> allCourses;

    public CourseRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        courseDao = database.courseDao();
        categoryDao = database.categoryDao();
        termDao = database.termDao();
        allCourses = courseDao.getAllCourses();
    } // end of constructor

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    } // end of getAllCourses

    public LiveData<Course> getCourseById(int courseId) {
        return courseDao.getCourseById(courseId);
    } // end of getCourseById

    public LiveData<List<Category>> getCategoriesByCourse(int courseId) {
        return categoryDao.getCategoriesByCourse(courseId);
    } // end of getCategoriesByCourse

    public void insertCourse(Course course, Application application) {
        new InsertCourseAsyncTask(courseDao, termDao, application).execute(course);
    } // end of insertCourse

    private static class InsertCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;
        private TermDao termDao;
        private Application application;

        private InsertCourseAsyncTask(CourseDao courseDao, TermDao termDao, Application application) {
            this.courseDao = courseDao;
            this.termDao = termDao;
            this.application = application;
        } // end of constructor

        @Override
        protected Void doInBackground(Course... courses) {
            int courseId = (int) courseDao.insertCourseAndGetId(courses[0]);

            // initialize default terms for the inserted course
            AppDatabase.initializeDefaultTerms(application, courseId);
            return null;
        }
    } // end of InsertCourseAsyncTask class
} // end of CourseRepository class
