package com.example.gradetrackerapp.course;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.example.gradetrackerapp.database.ActivityConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Term {
    private String termName;
    private double termGrade;

    @TypeConverters({ActivityConverter.class})
    private List<Activity> activities;

    public Term(String termName) {
        this.termName = termName;
        this.activities = new ArrayList<>();
    }

    public String getTermName() {
        return termName;
    }

    public double getTermGrade() {
        return termGrade;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public void setTermGrade(double termGrade) {
        this.termGrade = termGrade;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }
} // end of Term class
