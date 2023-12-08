package com.example.gradetrackerapp.grades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.gradetrackerapp.R;

public class FinalsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);

        TextView heading = view.findViewById(R.id.heading);
        String newHeading = "Finals";
        heading.setText(newHeading);

        return view;
    } // end of onCreateView
} // end of FinalsFragment class
