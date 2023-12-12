package com.example.gradetrackerapp.util;

import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Task;
import com.example.gradetrackerapp.data.ref.Term;

import java.util.List;

public class GradeCalculator {

    public GradeCalculator() {

    } // end of constructor

    protected int solvePredictedGradeFromButton(int classStandingWeight, int examWeight, List<Task> tasks) {
        // using stream to get the total userScore in the task list
        double userTotalScoreOnClassStanding = tasks.stream().mapToDouble(task -> task.score).sum();

        // using stream to get the total perfectScore in the task list
        double termTotalScoreOnClassStanding = tasks.stream().mapToDouble(task -> task.totalScore).sum();

        double newClassStandingWeight = (double) classStandingWeight / 100;

        // FORMULA
        double predictedGrade = ((((userTotalScoreOnClassStanding / termTotalScoreOnClassStanding)) * 50 + 50) * newClassStandingWeight) + examWeight;

        return (int) Math.round(predictedGrade);
    } // end of solvePredictedGradeFromButton

    protected int solvePredictedGradeFromCheckbox(Course course, Term term, List<Task> tasks) {
        int classStandingWeight = course.classStandingWeight;
        int examWeight = course.examWeight;

        // using stream to get the total userScore in the task list
        double userTotalScoreOnClassStanding = tasks.stream().mapToDouble(task -> task.score).sum();

        // using stream to get the total perfectScore in the task list
        double termTotalScoreOnClassStanding = tasks.stream().mapToDouble(task -> task.totalScore).sum();

        double newExamWeight = (double) examWeight / 100;
        double newClassStandingWeight = (double) classStandingWeight / 100;

        double examScore = (double) term.exam.score;
        double examTotalScore = (double) term.exam.totalScore;

        // FORMULA
        double finalGradeForTerm = ((((userTotalScoreOnClassStanding / termTotalScoreOnClassStanding)) * 50 + 50) * newClassStandingWeight) + ((((examScore / examTotalScore)) * 50 + 50) * newExamWeight);

        return (int) Math.round(finalGradeForTerm);
    } // end of solvePredictedGradeFromCheckbox

    protected int solveAndMakeFeedbackForTerm(Course course, Term term, List<Task> tasks) {
        int termTargetGrade = term.targetGrade;
        int examWeight = course.examWeight;
        int classStandingWeight = course.classStandingWeight;

        // using stream to get the total userScore in the task list
        double userTotalScoreOnClassStanding = tasks.stream().mapToDouble(task -> task.score).sum();

        // using stream to get the total perfectScore in the task list
        double termTotalScoreOnClassStanding = tasks.stream().mapToDouble(task -> task.totalScore).sum();

        // separate exam scored in integer values
        double examTotalScore = (double) term.exam.totalScore;

        // converting weights to percentages
        double newExamWeight = (double) examWeight / 100;
        double newClassStandingWeight = (double) classStandingWeight / 100;

        // FORMULA
        double requiredScore = ((-(userTotalScoreOnClassStanding) * (examTotalScore) * (newClassStandingWeight)) / ((termTotalScoreOnClassStanding) * (newExamWeight))) +
                (((examTotalScore) * (termTargetGrade)) / (50 * newExamWeight)) -
                (((examTotalScore) * (newClassStandingWeight)) / (newExamWeight)) -
                examTotalScore;

        return (int) Math.ceil(requiredScore);
    } // end of solveAndMakeFeedbackForTerm
} // end of GradeCalculator class
