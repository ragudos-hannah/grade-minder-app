package com.example.gradetrackerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.callback.OnEditClickListener;
import com.example.gradetrackerapp.callback.OnItemClickListener;
import com.example.gradetrackerapp.data.ref.Course;
import com.example.gradetrackerapp.data.ref.Task;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courses;
    private final LayoutInflater inflater;
    private OnItemClickListener<Course> listener;
    private OnEditClickListener<Course> editClickListener;

    public CourseAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        courses = new ArrayList<>();
    } // end of constructor

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    } // end of setCourses

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view, listener);
    } // end of onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course currentCourse = courses.get(position);
        holder.bind(currentCourse);
    } // end of onBindViewHolder

    @Override
    public int getItemCount() {
        return courses.size();
    } // end of getItemCount

    public void setOnItemClickListener(OnItemClickListener<Course> listener) {
        this.listener = listener;
    } // end of setOnItemClickListener

    public void setOnEditClickListener(OnEditClickListener<Course> listener) {
        this.editClickListener = listener;
    } // end of setOnEditClickListener

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseNameTextView;
        private final TextView courseCodeTextView;
        private final TextView courseInstructorTextView;
        private ImageView courseInfo;

        CourseViewHolder(@NonNull View itemView, OnItemClickListener<Course> listener) {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.courseNameTextView);
            courseCodeTextView = itemView.findViewById(R.id.courseCodeTextView);
            courseInstructorTextView = itemView.findViewById(R.id.courseInstructorTextView);
            courseInfo = itemView.findViewById(R.id.courseInfoButton);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(courses.get(position));
                    }
                }
            });

            courseInfo.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && editClickListener != null) {
                    editClickListener.onEditClick(courses.get(position));
                }
            });
        } // end of constructor

        void bind(Course course) {
            courseNameTextView.setText(course.courseName);
            courseCodeTextView.setText(course.courseCode);
            courseInstructorTextView.setText(course.courseInstructor);
        } // end of bind
    } // end of CourseViewHolder class
} // end of CourseAdapter class
