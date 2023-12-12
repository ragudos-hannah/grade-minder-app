package com.example.gradetrackerapp.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.gradetrackerapp.data.ref.Task;
import com.example.gradetrackerapp.data.ref.Term;
import com.example.gradetrackerapp.database.AppDatabase;
import com.example.gradetrackerapp.database.dao.TermDao;

public class TermRepository {
    private TermDao termDao;

    public TermRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        termDao = database.termDao();
    } // end of constructor

    public void updateTerm(Term term, Application application) {
        new UpdateTermAsyncTask(termDao, application).execute(term);
    } // end of updateTerm

    private static class UpdateTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;
        private Application application;

        private UpdateTermAsyncTask(TermDao termDao, Application application) {
            this.termDao = termDao;
            this.application = application;
        } // end of constructor

        @Override
        protected Void doInBackground(Term... terms) {
            AppDatabase.getInstance(application).updateTerm(terms[0]);

            return null;
        } // end of doInBackground
    } // end of UpdateTermAsyncTask
} // end of TermRepository class
