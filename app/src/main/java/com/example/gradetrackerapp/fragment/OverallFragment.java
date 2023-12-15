package com.example.gradetrackerapp.fragment;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Term;
import com.example.gradetrackerapp.data.repository.CourseRepository;
import com.example.gradetrackerapp.util.OverallCalculator;

import java.util.List;
import java.util.logging.Level;

public class OverallFragment extends Fragment {
    private CourseRepository courseRepository;
    private OverallCalculator calculator;
    private List<Term> terms;
    private final Course course;

    /* Components */
    private TextView prelimsGradeTV, midtermsGradeTV, finalsGradeTV, finalsFinalGradeTV, feedbackTV;
    private EditText finalsFinalGradeET;

    public OverallFragment(Course course) {
        this.course = course;
    } // end of constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize variables
        calculator = new OverallCalculator(requireActivity().getApplication());
        courseRepository = new CourseRepository(requireActivity().getApplication());
    } // end of onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overall_version3, container, false);

        prelimsGradeTV = view.findViewById(R.id.prelimsGradeTV);
        midtermsGradeTV = view.findViewById(R.id.midtermsGradeTV);
        finalsGradeTV = view.findViewById(R.id.finalsGradeTV);
        finalsFinalGradeTV = view.findViewById(R.id.finalsFinalGradeTV);
        feedbackTV = view.findViewById(R.id.overallFeedbackTV);
        finalsFinalGradeET = view.findViewById(R.id.targetFinalFinalGradeET);

        finalsFinalGradeET.setText(String.valueOf(course.targetGrade));
        finalsFinalGradeET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateTargetGrade(finalsFinalGradeET);
                calculateOverall();
                return true;
            }
            return false;
        });

        finalsFinalGradeET.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateTargetGrade(finalsFinalGradeET);
                calculateOverall();
            }
        });

        return view;
    } // end of onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        courseRepository.getTermsForCourse(course.courseId).observe(getViewLifecycleOwner(), terms -> {
            this.terms = terms;
            calculateOverall();
        });
    } // end of onViewCreated

    private void updateTargetGrade(EditText editText) {
        int targetGrade = Integer.parseInt(editText.getText().toString());
        if (targetGrade < 75 || targetGrade > 100) {
            editText.setText(String.valueOf(course.targetGrade));
            Toast.makeText(getContext(), "Target Grade should be in the 75 - 100 range only", Toast.LENGTH_SHORT).show();
        } else {
            course.targetGrade = targetGrade;
            courseRepository.updateCourse(course, requireActivity().getApplication());
        }
    } // end of updateTargetGrade

    private void calculateOverall() {
        if (course != null && terms != null && prelimsGradeTV != null && midtermsGradeTV != null && finalsGradeTV != null && finalsFinalGradeTV != null && feedbackTV != null) {
            calculator.calculateOverall(course, terms, prelimsGradeTV, midtermsGradeTV, finalsGradeTV, finalsFinalGradeTV, feedbackTV);
        }
    } // end of calculateOverall
} // end of OverallFragment class
