package com.example.gradetrackerapp.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.adapter.TaskAdapter;
import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Task;
import com.example.gradetrackerapp.data.ref.Term;
import com.example.gradetrackerapp.database.AppDatabase;
import com.example.gradetrackerapp.database.dao.TermDao;
import com.example.gradetrackerapp.util.FeedbackGenerator;
import com.example.gradetrackerapp.view_model.TaskViewModel;
import com.example.gradetrackerapp.view_model.TermViewModel;

import java.util.List;

public class TermFragment extends Fragment {
    private FeedbackGenerator feedbackGenerator;
    private TaskAdapter taskAdapter;
    private TermViewModel termViewModel;
    private TaskViewModel taskViewModel;
    private List<Task> tasks;
    private Course course;
    private final Term term;

    /* instance variables for components */
    TextView termGradeTextView, feedbackTextView;
    EditText examScoreEditTask, examTotalScoreEditTask, targetGradeEditText;
    Button addTaskButton;
    ImageButton solveButton;

    public TermFragment(Term term, Course course) {
        this.term = term;
        this.course = course;
    } // end of constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedbackGenerator = new FeedbackGenerator();

        // initialize view models
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
    } // end of onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_term, container, false);

        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        TermDao termDao = appDatabase.termDao();

        termGradeTextView = view.findViewById(R.id.termGradeTV);
        feedbackTextView = view.findViewById(R.id.feedbackTV);
        targetGradeEditText = view.findViewById(R.id.targetGradeEditText);
        examScoreEditTask = view.findViewById(R.id.examScoreEditTask);
        examTotalScoreEditTask = view.findViewById(R.id.examTotalScoreEditTask);

        // acquire the course given the term ID
        termDao.getCourseForTerm(term.termId).observe(getViewLifecycleOwner(), course -> {
            if (course != null) {
                this.course = course;
            }
        });

        // observe changes in the list of courses
        taskViewModel.getTasksForTerm(term.termId).observe(getViewLifecycleOwner(), tasks -> {
            this.tasks = tasks;
            taskAdapter.setTasks(tasks);
        });

        // add an event when the add task button is pressed
        addTaskButton = view.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(v -> showAddOrEditTaskDialog(null));

        // add an option to update the target Grade if user changes it
        targetGradeEditText.setText(String.valueOf(term.targetGrade));
        targetGradeEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateTargetGrade(targetGradeEditText);
                return true;
            }
            return false;
        });

        targetGradeEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateTargetGrade(targetGradeEditText);
            }
        });

        // add an event when the solve button is pressed
        solveButton = view.findViewById(R.id.solveButton);
        termGradeTextView = view.findViewById(R.id.termGradeTV);
        feedbackTextView = view.findViewById(R.id.feedbackTV);
        solveButton.setOnClickListener(v -> feedbackGenerator.generateFeedbackForBluePlayButton(course, term, tasks, termGradeTextView,feedbackTextView));

        return view;
    } // end of onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = requireView().findViewById(R.id.taskRecyclerView);
        if (recyclerView != null) {
            taskAdapter = new TaskAdapter(requireContext());
            taskAdapter.setOnDeleteClickListener(task -> taskViewModel.deleteTask(task));
            taskAdapter.setOnEditClickListener(this::showAddOrEditTaskDialog);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(taskAdapter);
        }

        updateButtonStates(term.examDone);
        taskAdapter.setTermFinalized(term.examDone);

        // marks the checkbox if exam is already done
        CheckBox examCheckbox = view.findViewById(R.id.examCheckbox);
        examCheckbox.setChecked(term.examDone);

        // sets the initial text views as empty if termGrade is 0
        if (term.termGrade == 0) {
            termGradeTextView.setText("0");
            feedbackTextView.setText("");
        } else {
            String feedback = feedbackGenerator.getFeedbackFromCheckbox(term.targetGrade, term.termGrade);

            examScoreEditTask.setText(String.valueOf(term.exam.score));
            examTotalScoreEditTask.setText(String.valueOf(term.exam.totalScore));
            termGradeTextView.setText(String.valueOf(term.termGrade));
            feedbackTextView.setText(feedback);
        }

        examCheckbox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                String examScoreString = examScoreEditTask.getText().toString().trim();
                String examTotalScoreString = examTotalScoreEditTask.getText().toString().trim();

                if (!examScoreString.isEmpty() && !examTotalScoreString.isEmpty()) {
                    int examScore = Integer.parseInt(examScoreEditTask.getText().toString());
                    int examTotalScore = Integer.parseInt(examTotalScoreEditTask.getText().toString());

                    if (examScore <= examTotalScore && examTotalScore != 0) {
                        int termGrade = feedbackGenerator.generateFeedbackForCheckbox(course, term, examScore, examTotalScore, tasks, termGradeTextView, feedbackTextView);
                        updateExamGrade(examScore, examTotalScore, termGrade);
                        term.examDone = true;
                    } else if (examScore > examTotalScore && examTotalScore != 0) {
                        Toast.makeText(getContext(), "Invalid exam score, score should be equal or less than the total exam score", Toast.LENGTH_LONG).show();
                        term.examDone = false;
                        examCheckbox.setChecked(false);
                    } else {
                        Toast.makeText(getContext(), "Invalid total exam score. Score should be greater than zero", Toast.LENGTH_LONG).show();
                        examCheckbox.setChecked(false);
                    }
                } else {
                    Toast.makeText(getContext(), "You need to add your exam score and it's corresponding total score first", Toast.LENGTH_LONG).show();
                    examCheckbox.setChecked(false);
                }
            } else {
                term.exam.score = 100;
                term.exam.totalScore = 100;
                term.termGrade = 0;
                term.examDone = false;
            }
            termViewModel.updateTerm(term);
        }));
    } // end of onViewCreated

    private void updateButtonStates(boolean isChecked) {
        addTaskButton.setEnabled(!isChecked);
        targetGradeEditText.setEnabled(!isChecked);
        solveButton.setEnabled(!isChecked);
        examScoreEditTask.setEnabled(!isChecked);
        examTotalScoreEditTask.setEnabled(!isChecked);
    } // end of updateButtonStates

    private void updateTargetGrade(EditText editText) {
        int targetGrade = Integer.parseInt(editText.getText().toString());
        if (targetGrade < 75 || targetGrade > 100) {
            editText.setText(String.valueOf(term.targetGrade));
            Toast.makeText(getContext(), "Target Grade should be in the 75 - 100 range only", Toast.LENGTH_SHORT).show();
        } else {
            term.targetGrade = targetGrade;
            termViewModel.updateTerm(term);
        }
    } // end of updateTargetGrade

    private void updateExamGrade(int examScore, int totalExamScore, int termGrade) {
        term.exam.score = examScore;
        term.exam.totalScore = totalExamScore;
        term.termGrade = termGrade;
        termViewModel.updateTerm(term);
    } // end of updateExamGrade

    private void showAddOrEditTaskDialog(Task existingTask) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_edit_task, null);
        builder.setView(dialogView);

        EditText taskNameET = dialogView.findViewById(R.id.taskNameET);
        EditText scoreET = dialogView.findViewById(R.id.scoreET);
        EditText totalScoreET = dialogView.findViewById(R.id.totalScoreET);
        Button cancelBTN = dialogView.findViewById(R.id.cancelBTN);
        Button checkBTN = dialogView.findViewById(R.id.checkBTN);

        if (existingTask != null) {
            taskNameET.setText(existingTask.taskName);
            scoreET.setText(String.valueOf(existingTask.score));
            totalScoreET.setText(String.valueOf(existingTask.totalScore));
        }

        AlertDialog dialog = builder.create();

        cancelBTN.setOnClickListener(view -> dialog.dismiss());

        checkBTN.setOnClickListener(view -> {
            String taskName = taskNameET.getText().toString().trim();
            int score = Integer.parseInt(scoreET.getText().toString().trim());
            int totalScore = Integer.parseInt(totalScoreET.getText().toString().trim());

            if (!TextUtils.isEmpty(taskName) && !TextUtils.isEmpty(String.valueOf(score)) && !TextUtils.isEmpty(String.valueOf(totalScore))) {
                Task newTask = new Task();
                newTask.taskName = taskName;
                newTask.score = score;
                newTask.totalScore = totalScore;

                // set the term id for the new task
                newTask.termId = term.termId;

                // Insert the new task using the ViewModel
                if (existingTask != null) {
                    newTask.taskId = existingTask.taskId;
                    taskViewModel.updateTask(newTask);
                } else {
                    taskViewModel.insertTask(newTask);
                }
            } else {
                Toast.makeText(getContext(), "Invalid input. Task not added.", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        dialog.show();
    } // end of showTaskDialog
} // end of PrelimsFragment class