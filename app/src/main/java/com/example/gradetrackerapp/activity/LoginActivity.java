package com.example.gradetrackerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.authentication.BiometricHelper;

import java.io.File;

public class LoginActivity extends AppCompatActivity implements BiometricHelper.BiometricCallback {
    private static final int AUTHENTICATION_REQUEST_CODE = 123;
    private static final int AUTHENTICATION_REQUEST_DELETION_CODE = 321;
    private boolean proceedToLogin = true;
    private int invalidAttemptCount = 0;
    private static final int maxInvalidAttempts = 5;
    private BiometricHelper biometricHelper;
    private BiometricHelper.BiometricCallback biometricCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        biometricHelper = new BiometricHelper(this, this);
        biometricCallback = this;

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            if (proceedToLogin) {
                biometricHelper.initiateBiometricAuthentication();
            } else {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        TextView removeButton = findViewById(R.id.removeButton);
        removeButton.setOnClickListener(v -> {
            biometricHelper.initiateAuthenticationForDeletion();
        });
    } // end of onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTHENTICATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // User successfully authenticated with device password
                biometricCallback.onBiometricAuthenticationSucceeded();
            } else {
                // Authentication failed or user canceled
                biometricCallback.onBiometricAuthenticationFailed("Password authentication failed.");
            }
        }

        if (requestCode == AUTHENTICATION_REQUEST_DELETION_CODE) {
            if (resultCode == RESULT_OK) {
                biometricCallback.onBiometricAuthenticationForDeletionSucceeded();
            } else {
                biometricCallback.onBiometricAuthenticationFailed("Password authentication failed.");
            }
        }
    } // end of onActivityResult

    @Override
    public void onBiometricAuthenticationSucceeded() {
        proceed();
    } // end of onBiometricAuthenticationSucceeded

    @Override
    public void onBiometricAuthenticationForDeletionSucceeded() {
        wipeData();
    } // end of onBiometricAuthenticationForDeletionSucceeded

    @Override
    public void onBiometricAuthenticationFailed(String message) {
        showToast(message);
        if (invalidAttemptCount >= maxInvalidAttempts) {
            showToast("Exceeded maximum invalid attempts. Access restricted.");
            invalidAttemptCount = 0;
        } else {
            invalidAttemptCount++;
        }
    } // end of onBiometricAuthenticationFailed

    private void proceed() {
        Intent proceedIntent = new Intent(LoginActivity.this, HomepageActivity.class);
        startActivity(proceedIntent);
    } // end of proceed

    private void wipeData() {
        File file = new File(getFilesDir(), "register_data/data.txt");

        if (file.delete()) {
            showToast("Data file removed successfully");
        } else {
            showToast("Failed to remove data file");
        }

        File dbFile = getDatabasePath("course-database");
        if (dbFile.delete()) {
            showToast("Database file removed successfully");
        } else {
            showToast("Failed to remove database file");
        }
        proceedToLogin = false;
    } // end of removeDateFile

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    } // end of showToast
} // end of LoginActivity
