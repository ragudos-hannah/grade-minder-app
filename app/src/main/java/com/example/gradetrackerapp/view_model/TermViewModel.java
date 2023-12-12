package com.example.gradetrackerapp.view_model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gradetrackerapp.data.ref.Term;
import com.example.gradetrackerapp.data.repository.TermRepository;

public class TermViewModel extends AndroidViewModel {
    private TermRepository termRepository;
    private LiveData<LiveData<Term>> terms;

    public TermViewModel(Application application) {
        super(application);
        termRepository = new TermRepository(application);
    } // end of TermViewModel

    public void updateTerm(Term term) {
        termRepository.updateTerm(term, getApplication());
    } // end of updateTerm
} // end of TermViewModel class
