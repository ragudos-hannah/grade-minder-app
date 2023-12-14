package com.example.gradetrackerapp.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gradetrackerapp.data.repository.CourseRepository;
import com.example.gradetrackerapp.data.ref.Course;

import java.util.List;

public class HomepageViewModel extends AndroidViewModel {
    private CourseRepository courseRepository;

    public HomepageViewModel(@NonNull Application application) {
        super(application);

        courseRepository = new CourseRepository(application);
    } // end of constructor

    public LiveData<List<Course>> getCourses() {
        return courseRepository.getAllCourses();
    } // end of getLiveDataCourses

    public void insertCourse(Course course) {
        courseRepository.insertCourse(course, getApplication());
    } // end of insertCourse


    public void deleteCourse(Course course) {
        courseRepository.deleteCourse(course);
    } // end of deleteCourse

    public void updateCourse(Course course) {
        courseRepository.updateCourse(course, getApplication());
    } // end of updateCourse
} // end of HomepageViewModel class
