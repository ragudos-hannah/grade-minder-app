package com.example.gradetrackerapp.grades;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gradetrackerapp.data.category.Category;
import com.example.gradetrackerapp.data.course.Course;
import com.example.gradetrackerapp.data.course.CourseRepository;

import java.util.List;

public class GradeViewModel extends AndroidViewModel {
    private CourseRepository courseRepository;
    private LiveData<List<Category>> categoriesLiveData;

    public GradeViewModel(Application application, int courseId) {
        super(application);
        courseRepository = new CourseRepository(application);
        categoriesLiveData = courseRepository.getCategoriesByCourse(courseId);
    }

    public LiveData<List<Category>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public LiveData<Course> getCourseById(int courseId) {
        return courseRepository.getCourseById(courseId);
    }
} // end of GradeViewModel
