package com.example.gradetrackerapp.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import com.example.gradetrackerapp.data.ref.Task;
import com.example.gradetrackerapp.database.AppDatabase;
import com.example.gradetrackerapp.database.dao.TaskDao;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;

    public TaskRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        taskDao = database.taskDao();
    } // end of constructor

    public void insertTask(Task task, Application application) {
        new InsertTaskAsyncTask(taskDao, application).execute(task);
    } // end of insertTask

    public void deleteTask(Task task) {
        new DeleteTaskAsyncTask(taskDao).execute(task);
    } // end of deleteTask

    public void updateTask(Task task, Application application) {
        new UpdateTaskAsyncTask(taskDao, application).execute(task);
    } // end of updateTask

    public LiveData<List<Task>> getTasksForTerm(int termId) {
        return taskDao.getTasksForTerm(termId);
    } // end of getTasksForTerm

    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        private Application application;

        private InsertTaskAsyncTask(TaskDao taskDao, Application application) {
            this.taskDao = taskDao;
            this.application = application;
        } // end of constructor

        @Override
        protected Void doInBackground(Task... tasks) {
            AppDatabase.getInstance(application).insertTask(tasks[0]);

            return null;
        } // end of doInBackground
    } // end of InsertTaskAsyncTask

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        } // end of constructor

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.deleteTask(tasks[0]);

            return null;
        } // end of doInBackground
    } // end of DeleteTaskAsyncTask

    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        private Application application;

        private UpdateTaskAsyncTask(TaskDao taskDao, Application application) {
            this.taskDao = taskDao;
            this.application = application;
        } // end of constructor

        @Override
        protected Void doInBackground(Task... tasks) {
            AppDatabase.getInstance(application).updateTask(tasks[0]);
            return null;
        } // end of doInBackground
    } // end of UpdateTaskAsyncTask
} // end of TaskRepository class
