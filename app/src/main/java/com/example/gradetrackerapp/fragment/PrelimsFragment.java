package com.example.gradetrackerapp.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

import java.util.List;

public class PrelimsFragment extends Fragment {
    private TaskViewModel taskViewModel;
    private Term term;
    private Course course;

    public PrelimsFragment(Term term, Course course) {
        this.term = term;
        this.course = course;
    } // end of constructor

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

        // initialize view model
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // observe changes in the list of courses
        taskViewModel.getTasksForTerm(term.termId).observe(getViewLifecycleOwner(), this::updateRecyclerView);

        Button addTaskButton = view.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(v -> showAddTaskDialog());

        return view;
    } // end of onCreateView

    private void updateRecyclerView(List<Task> tasks) {
        TaskAdapter taskAdapter = new TaskAdapter(requireContext());
        taskAdapter.setOnDeleteClickListener(new TaskAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Task task) {
                taskViewModel.deleteTask(task);
            }
        });
        taskAdapter.setTasks(tasks);

        RecyclerView recyclerView = getView().findViewById(R.id.taskRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(taskAdapter);
    } // end of updateRecyclerView

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_edit_task, null);
        builder.setView(dialogView);

        EditText taskNameET = dialogView.findViewById(R.id.taskNameET);
        EditText scoreET = dialogView.findViewById(R.id.scoreET);
        EditText totalScoreET = dialogView.findViewById(R.id.totalScoreET);
        Button cancelBTN = dialogView.findViewById(R.id.cancelBTN);
        Button checkBTN = dialogView.findViewById(R.id.checkBTN);

        AlertDialog dialog = builder.create();

        cancelBTN.setOnClickListener(view -> builder.create().dismiss());

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
                taskViewModel.insertTask(newTask);
            } else {
                Toast.makeText(getContext(), "Invalid input. Task not added.", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        dialog.show();
    } // end of showTaskDialog
} // end of PrelimsFragment class