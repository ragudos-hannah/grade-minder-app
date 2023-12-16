package com.example.gradetrackerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gradetrackerapp.R;
import com.example.gradetrackerapp.activity.LoginActivity;
import com.example.gradetrackerapp.authentication.BiometricHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity implements BiometricHelper.BiometricCallback {
    private static final int AUTHENTICATION_REQUEST_CODE = 123;
    private int invalidAttemptCount = 0;
    private static final int maxInvalidAttempts = 5;
    private BiometricHelper biometricHelper;
    private BiometricHelper.BiometricCallback biometricCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        biometricHelper = new BiometricHelper(this, this);
        biometricCallback = this;

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            biometricHelper.initiateBiometricAuthentication();
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
    } // end of onActivityResult

    @Override
    public void onBiometricAuthenticationSucceeded() {
        saveUserDetails();
        showToast("Biometric authentication succeeded.");
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
    } // end of onBiometricAuthenticationSucceeded

    @Override
    public void onBiometricAuthenticationForDeletionSucceeded() {
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

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    } // end of showToast

    private void saveUserDetails() {
        EditText nameET = findViewById(R.id.nameTextInputEditText);
        EditText surnameET = findViewById(R.id.surnameTextInputEditText);

        try {
            File directory = new File(getFilesDir(), "register_data");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, "data.txt");


            FileWriter fileWriter = new FileWriter(file, false);

            fileWriter.write(nameET.getText().toString() + " " + surnameET.getText().toString());
            fileWriter.close();

            showToast("User details saved successfully");

        } catch (IOException exception) {
            showToast("Error saving user details");
            exception.printStackTrace();
        }
    } // end of saveUserDetails
} // end of SignUpActivity class
