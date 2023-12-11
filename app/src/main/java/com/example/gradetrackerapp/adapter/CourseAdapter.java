package com.example.gradetrackerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.data.ref.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courses;
    private final LayoutInflater inflater;
    private OnItemClickListener listener;

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

    public interface OnItemClickListener {
        void onItemClick(Course course);
    } // end of OnItemClickListener

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    } // end of setOnItemClickListener

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseNameTextView;
        private final TextView courseCodeTextView;
        private final TextView courseInstructorTextView;

        CourseViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.courseNameTextView);
            courseCodeTextView = itemView.findViewById(R.id.courseCodeTextView);
            courseInstructorTextView = itemView.findViewById(R.id.courseInstructorTextView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(courses.get(position));
                    }
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
