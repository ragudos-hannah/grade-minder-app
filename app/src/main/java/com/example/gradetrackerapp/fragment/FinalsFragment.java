package com.example.gradetrackerapp.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.gradetrackerapp.view_model.TaskViewModel;
import com.example.gradetrackerapp.view_model.TermViewModel;

import java.util.List;

public class FinalsFragment extends Fragment {
    private TaskAdapter taskAdapter;
    private TermViewModel termViewModel;
    private TaskViewModel taskViewModel;
    private Term term;
    private Course course;

    public FinalsFragment(Term term, Course course) {
        this.term = term;
        this.course = course;
    } // end of constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    } // end of onCreate

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.taskRecyclerView);
        if (recyclerView != null) {
            taskAdapter = new TaskAdapter(requireContext());
            taskAdapter.setOnDeleteClickListener(task -> taskViewModel.deleteTask(task));
            taskAdapter.setOnEditClickListener(task -> showAddOrEditTaskDialog(task));
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(taskAdapter);
        }

        updateButtonStates(term.examDone);
        taskAdapter.setTermFinalized(term.examDone);

        // marks the checkbox if exam is already done
        CheckBox examCheckbox = view.findViewById(R.id.examCheckbox);
        examCheckbox.setChecked(term.examDone);

        examCheckbox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            term.examDone = isChecked;
            termViewModel.updateTerm(term);
        }));
    } // end of onViewCreated

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_term, container, false);

        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        TermDao termDao = appDatabase.termDao();

        // acquire the course given the term ID
        termDao.getCourseForTerm(term.termId).observe(getViewLifecycleOwner(), course -> {
            if (course != null) {
                this.course = course;
            }
        });

        // initialize view models
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        // observe changes in the list of courses
        taskViewModel.getTasksForTerm(term.termId).observe(getViewLifecycleOwner(), tasks -> taskAdapter.setTasks(tasks));

        // add an event when the add task button is pressed
        Button addTaskButton = view.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(v -> showAddOrEditTaskDialog(null));

        return view;
    } // end of onCreateView

    private void updateButtonStates(boolean isChecked) {
        Button addTaskButton = requireView().findViewById(R.id.addTaskButton);
        EditText targetGradeEditText = requireView().findViewById(R.id.targetGradeEditText);
        ImageButton solveButton = requireView().findViewById(R.id.solveButton);
        EditText examScoreEditTask = requireView().findViewById(R.id.examScoreEditTask);
        EditText examTotalScoreEditTask = requireView().findViewById(R.id.examTotalScoreEditTask);

        addTaskButton.setEnabled(!isChecked);
        targetGradeEditText.setEnabled(!isChecked);
        solveButton.setEnabled(!isChecked);
        examScoreEditTask.setEnabled(!isChecked);
        examTotalScoreEditTask.setEnabled(!isChecked);
    } // end of updateButtonStates

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
} // end of FinalsFragment class
