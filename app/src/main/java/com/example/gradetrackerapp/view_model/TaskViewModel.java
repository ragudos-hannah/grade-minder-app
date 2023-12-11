package com.example.gradetrackerapp.view_model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gradetrackerapp.data.ref.Task;
import com.example.gradetrackerapp.data.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private LiveData<List<Task>> tasks;

    public TaskViewModel(Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
    } // end of TaskViewModel

    public void insertTask(Task task) {
        taskRepository.insertTask(task, getApplication());
    } // end of insertTask

    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    } // end of deleteTask

    public LiveData<List<Task>> getTasksForTerm(int termId) {
        return taskRepository.getTasksForTerm(termId);
    } // end of getTasksForTerm
} // end of TaskViewModel class
