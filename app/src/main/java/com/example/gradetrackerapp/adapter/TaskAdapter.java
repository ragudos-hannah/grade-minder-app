package com.example.gradetrackerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.callback.OnDeleteClickListener;
import com.example.gradetrackerapp.callback.OnEditClickListener;
import com.example.gradetrackerapp.callback.OnItemClickListener;
import com.example.gradetrackerapp.data.ref.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private boolean isTermFinalized = false;
    private List<Task> tasks;
    private final LayoutInflater inflater;
    private OnItemClickListener<Task> listener;
    private OnDeleteClickListener<Task> deleteClickListener;
    private OnEditClickListener<Task> editClickListener;

    public TaskAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        tasks = new ArrayList<>();
    } // end of constructor

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    } // end of setTasks

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view, listener);
    } // end of onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.bind(currentTask);
    } // end of onBindViewHolder

    @Override
    public int getItemCount() {
        return tasks.size();
    } // end of getItemCount

    public void setTermFinalized(boolean isFinalized) {
        isTermFinalized = isFinalized;
        notifyDataSetChanged();
    } // end of setTermFinalized

    public void setOnItemClickListener(OnItemClickListener<Task> listener) {
        this.listener = listener;
    } // end of setOnItemClickListener

    public void setOnDeleteClickListener(OnDeleteClickListener<Task> listener) {
        this.deleteClickListener = listener;
    } // end of setOnDeleteClickListener

    public void setOnEditClickListener(OnEditClickListener<Task> listener) {
        this.editClickListener = listener;
    } // end of setOnEditClickListener

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView taskNameTextView;
        private TextView scoreTextView;
        private TextView totalScoreTextView;
        private ImageButton editButton;
        private ImageButton deleteButton;

        TaskViewHolder(@NonNull View itemView, OnItemClickListener<Task> listener) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            totalScoreTextView = itemView.findViewById(R.id.totalScoreTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(tasks.get(position));
                    }
                }
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && deleteClickListener != null && !isTermFinalized) {
                    deleteClickListener.onDeleteClick(tasks.get(position));
                }
            });

            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && editClickListener != null && !isTermFinalized) {
                    editClickListener.onEditClick(tasks.get(position));
                }
            });
        } // end of constructor

        void bind(Task task) {
            taskNameTextView.setText(task.taskName);
            scoreTextView.setText(String.valueOf(task.score));
            totalScoreTextView.setText(String.valueOf(task.totalScore));
        } // end of bind
    } // end of TaskViewHolder class
} // end of TaskAdapter
