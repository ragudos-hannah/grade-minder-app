package com.example.gradetrackerapp.util;

import android.app.Application;
import android.widget.TextView;

import com.example.gradetrackerapp.activity.GradeActivity;
import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Term;
import com.example.gradetrackerapp.data.repository.CourseRepository;

import java.util.List;

public class OverallCalculator {
    private CourseRepository courseRepository;
    private Application application;

    public OverallCalculator(Application application) {
        this.application = application;
        courseRepository = new CourseRepository(application);
    } // end of OverallCalculator

    public void calculateOverall(Course course, List<Term> terms, TextView prelimsTV, TextView midtermsTV, TextView finalsTV, TextView finalsFinalsTV, TextView feedbackTV) {
        // obtain the exam status of all terms
        boolean prelimsExamDone = terms.get(0).examDone;
        boolean midtermsExamDone = terms.get(1).examDone;
        boolean finalsExamDone = terms.get(2).examDone;

        // obtain the term grades
        int prelimsGrade = terms.get(0).termGrade;
        int midtermsGrade = terms.get(1).termGrade;
        int finalsGrade = terms.get(2).termGrade;

        // if neither of the three terms are done
        if (!prelimsExamDone && !midtermsExamDone && !finalsExamDone) {
            prelimsTV.setText(String.valueOf(course.targetGrade));
            midtermsTV.setText(String.valueOf(course.targetGrade));
            finalsTV.setText(String.valueOf(course.targetGrade));

            feedbackTV.setText("You need at least " + course.targetGrade + " for all terms to achieve the target grade.");
        }

        // if only the prelim term is finished
        else if (prelimsExamDone && !midtermsExamDone && !finalsExamDone) {
            int average = (int) Math.ceil((double) ((3 * course.targetGrade) - prelimsGrade) / 2);

            prelimsTV.setText(String.valueOf(prelimsGrade));
            midtermsTV.setText(String.valueOf(average));
            finalsTV.setText(String.valueOf(average));

            if (average > 100) {
                feedbackTV.setText("The target grade is too high. Try Lowering it.");
            } else {
                feedbackTV.setText("You need at least " + average + " for midterms and finals to achieve the target grade.");
            }
        }

        // if the prelim and midterms are finished
        else if (prelimsExamDone && midtermsExamDone && !finalsExamDone) {
            int average = (int) Math.ceil((double) (((3 * course.targetGrade) - prelimsGrade - midtermsGrade)));

            prelimsTV.setText(String.valueOf(prelimsGrade));
            midtermsTV.setText(String.valueOf(midtermsGrade));
            finalsTV.setText(String.valueOf(average));

            if (average > 100) {
                feedbackTV.setText("The target grade is too high. Try Lowering it.");
            } else {
                feedbackTV.setText("You need at least " + average + " for finals to achieve the target grade.");
            }
        }

        // if all terms are finished
        else if (prelimsExamDone && midtermsExamDone && finalsExamDone) {
            int courseGrade = (int) Math.round((double) (prelimsGrade + midtermsGrade + finalsGrade) / (3));

            course.courseGrade = courseGrade;
            courseRepository.updateCourse(course, application);

            prelimsTV.setText(String.valueOf(prelimsGrade));
            midtermsTV.setText(String.valueOf(midtermsGrade));
            finalsTV.setText(String.valueOf(finalsGrade));
            finalsFinalsTV.setText(String.valueOf(courseGrade));

            if (courseGrade < course.targetGrade) {
                if (courseGrade >= 75) {
                    feedbackTV.setText("Just fell short but hey! You passed the 75 mark!");
                } else {
                    feedbackTV.setText("Sorry but you failed this course.");
                }
            } else {
                feedbackTV.setText("Congratulations! You have reached your target grade for this course!");
            }
        }

        else {
            prelimsTV.setText("?");
            midtermsTV.setText("?");
            finalsTV.setText("?");
            finalsFinalsTV.setText("");
            feedbackTV.setText("Error. previous terms should be finished before the later terms");
        }
    } // end of calculateOverall
} // end of OverallCalculator
