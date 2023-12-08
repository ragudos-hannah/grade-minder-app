package com.example.gradetrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gradetrackerapp.authentication.LoginActivity;
import com.example.gradetrackerapp.authentication.RegisterActivity;

import java.io.File;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (readFile()) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            Intent registerIntent = new Intent(this, RegisterActivity.class);
            startActivity(registerIntent);
        }

        finish();
    } // end of onCreate

    private boolean readFile() {
        File file = new File(getFilesDir(), "register_data/data.txt");
        Log.d("FileLocation", "File saved at: " + file.getAbsolutePath());
        return file.exists();
    }
} // end of Launcher class
