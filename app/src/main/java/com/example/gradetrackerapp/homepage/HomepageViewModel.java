package com.example.gradetrackerapp.homepage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gradetrackerapp.data.course.Course;
import com.example.gradetrackerapp.data.course.CourseRepository;

import java.util.List;

public class HomepageViewModel extends AndroidViewModel {
    private LiveData<List<Course>> coursesLiveData;
    private CourseRepository courseRepository;

    public HomepageViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        coursesLiveData = courseRepository.getAllCourses();
    } // end of constructor

    public LiveData<List<Course>> getCoursesLiveData() {
        return coursesLiveData;
    } // end of getCoursesLiveData

    public void insertCourse(Course course) {
        courseRepository.insertCourse(course, getApplication());
    } // end of insertCourse
} // end of HomepageViewModel class
