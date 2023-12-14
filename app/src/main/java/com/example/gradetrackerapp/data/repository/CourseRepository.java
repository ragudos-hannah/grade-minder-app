package com.example.gradetrackerapp.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Term;
import com.example.gradetrackerapp.database.AppDatabase;
import com.example.gradetrackerapp.database.dao.CourseDao;

import java.util.List;

public class CourseRepository {
    private CourseDao courseDao;

    public CourseRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        courseDao = database.courseDao();
    } // end of constructor

    public void insertCourse(Course course, Application application) {
        new InsertCourseAsyncTask(courseDao, application).execute(course);
    } // end of insertCourse

    public void updateCourse(Course course, Application application) {
        new UpdateCourseAsyncTask(courseDao, application).execute(course);
    } // end of updateCourse

    public LiveData<List<Course>> getAllCourses() {
        return courseDao.getAllCourses();
    } // end of getAllCourses

    public LiveData<Course> getCourseById(int courseId) {
        return courseDao.getCourseById(courseId);
    } // end of getCourseById

    public LiveData<List<Term>> getTermsForCourse(int courseId) {
        return courseDao.getTermsForCourse(courseId);
    } // end of getTermsForCourse

    private static class InsertCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;
        private Application application;

        private InsertCourseAsyncTask(CourseDao courseDao, Application application) {
            this.courseDao = courseDao;
            this.application = application;
        } // end of constructor

        @Override
        protected Void doInBackground(Course... courses) {
            AppDatabase.getInstance(application).insertCourse(courses[0]);

            return null;
        }
    } // end of InsertCourseAsyncTask class

    private static class UpdateCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;
        private Application application;

        private UpdateCourseAsyncTask(CourseDao courseDao, Application application) {
            this.courseDao = courseDao;
            this.application = application;
        } // end of constructor

        @Override
        protected Void doInBackground(Course... courses) {
            AppDatabase.getInstance(application).updateCourse(courses[0]);

            return null;
        } // end of doInBackground
    } // end of UpdateCourseAsyncTask class
} // end of CourseRepository class
