package com.example.gradetrackerapp.course;

import java.util.ArrayList;
import java.util.List;

public class Course {
    public String courseCode;
    public String courseName;
    public String professor;

    public Categories categories;

    public Course(String courseCode, String courseName, String professor) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.professor = professor;
    } // end of constructor

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getProfessor() {
        return professor;
    }

    public static class Categories {
        public Category classStanding;
        public Category exam;
    } // end of Categories class

    public static class Category {
        public Term prelims;
        public Term midterms;
        public Term finals;
    } // end of Category class

    public static class Term {
        public double termGrade = 0;

        public List<Activity> activities = new ArrayList<>();

        public void addActivity(String activityName, int activityScore) {
            activities.add(new Activity(activityName, activityScore));
        } // end of addActivity

        public void setTermGrade(double termGrade) {
            this.termGrade = termGrade;
        } // end of setTermGrade

        public List<Activity> getActivities() {
            return activities;
        } // end of getActivities
    } // end of Term class

    public static class Activity {
        public String activityName;
        public int activityScore;

        public Activity(String activityName, int activityScore) {
            this.activityName = activityName;
            this.activityScore = activityScore;
        } // end of constructor
    } // end of Activity class
} // end of Course class
