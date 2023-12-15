package com.example.gradetrackerapp.view_model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Term;
import com.example.gradetrackerapp.data.repository.CourseRepository;

import java.util.List;

public class GradeViewModel extends AndroidViewModel {
    private CourseRepository courseRepository;
    private LiveData<List<Term>> termsLiveData;

    public GradeViewModel(Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
    } // end of constructor

    public LiveData<List<Term>> getTerms(int courseId) {
        return courseRepository.getTermsForCourse(courseId);
    } // end of getTerms

    public LiveData<Course> getCourseById(int courseId) {
        return courseRepository.getCourseById(courseId);
    } // end of getCourseById

    public void updateCourse(Course course) {
        courseRepository.updateCourse(course, getApplication());
    } // end of updateCourse
} // end of GradeViewModel
