package com.example.gradetrackerapp.util;

import android.widget.TextView;

import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Exam;
import com.example.gradetrackerapp.data.ref.Task;
import com.example.gradetrackerapp.data.ref.Term;

import org.w3c.dom.Text;

import java.util.List;

public class FeedbackGenerator {
    private GradeCalculator calculator;

    public FeedbackGenerator() {
        calculator = new GradeCalculator();
    }


    public void generateFeedbackForCheckbox(Course course, Term term, List<Task> tasks, TextView predictedGradeTextView, TextView feedbackTextView) {
        int termTargetGrade = term.targetGrade;
        int classStandingWeight = course.classStandingWeight;
        int examWeight = course.examWeight;

        int finalGradeForTheTerm = calculator.solvePredictedGradeFromCheckbox(course, term, tasks);

        if (finalGradeForTheTerm < termTargetGrade) {
            if (finalGradeForTheTerm >= 75) {
                feedbackTextView.setText("Just fell short but hey! You passed the 75 mark!");
            } else {
                feedbackTextView.setText("Just fell short. Let's get it next time. I know you can do it!");
            }
        } else {
            feedbackTextView.setText("Yeyyy! You passed this term! Keep up the good work!");
        }
        predictedGradeTextView.setText(String.valueOf(finalGradeForTheTerm));
    } // end of generateFeedbackForCheckbox

    public void generateFeedbackForBluePlayButton(Course course, Term term, List<Task> tasks, TextView predictedGradeTextView, TextView feedbackTextView) {

        int scoreRequired = calculator.solveAndMakeFeedbackForTerm(course, term, tasks);
        int predictedGrade = calculator.solvePredictedGradeFromButton(course.classStandingWeight, course.examWeight, tasks);

        if (scoreRequired <= 0) {
            feedbackTextView.setText("Congratulations! You can expect that you can pass this term!");
        } else if (scoreRequired > term.exam.score) {
            feedbackTextView.setText("I am sorry, target grade is too high to reach. You might want to edit it?");
        } else {
            feedbackTextView.setText("You need at least " + scoreRequired + " to pass your target term grade if the exam is over " + term.exam.totalScore + ".");
        }
        predictedGradeTextView.setText(String.valueOf(predictedGrade));
    } // end of generateFeedbackForBluePlayButton
} // end of FeedbackGenerator class
