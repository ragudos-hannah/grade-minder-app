package com.example.gradetrackerapp.course;

import androidx.room.Entity;

@Entity
public class Activity {
    private String activityName;
    private int activityScore;

    public Activity(String activityName, int activityScore) {
        this.activityName = activityName;
        this.activityScore = activityScore;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getActivityScore() {
        return activityScore;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setActivityScore(int activityScore) {
        this.activityScore = activityScore;
    }
} // end of Activity class
