package com.example.gradetrackerapp.authentication;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BiometricHelper {
    private boolean authenticationFailed = false;
    private final Context context;
    private final BiometricCallback biometricCallback;
    private final Handler mainHandler;

    public BiometricHelper(Context context, BiometricCallback biometricCallback) {
        this.context = context;
        this.biometricCallback = biometricCallback;
        this.mainHandler = new Handler(Looper.getMainLooper());
    } // end of constructor

    public void initiateBiometricAuthentication() {
        BiometricManager biometricManager = BiometricManager.from(context);

        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                showBiometricPrompt();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                biometricCallback.onBiometricAuthenticationFailed("Biometric not enrolled.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                biometricCallback.onBiometricAuthenticationFailed("Biometric authentication not available.");
                break;
        }
    } // end of initiateBiometricAuthentication

    private void showBiometricPrompt() {
        Executor executor = Executors.newSingleThreadExecutor();

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Place your finger on the sensor.")
                .setNegativeButtonText("Cancel")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Log.e("BiometricHelper", "Biometric authentication error: " + errString);
                        biometricCallback.onBiometricAuthenticationFailed("Biometric authentication error: " + errString);
                    }

                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        biometricCallback.onBiometricAuthenticationSucceeded();
                    }

                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        if (!authenticationFailed) {
                            authenticationFailed = true;
                            biometricCallback.onBiometricAuthenticationFailed("Biometric authentication failed.");
                        }
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    } // end of showBiometricPrompt

    private void showToastOnMainThread(final String message) {
        mainHandler.post(() -> showToast(message));
    } // end of showToastOnMainThread

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    } // end of showToast

    public interface BiometricCallback {
        void onBiometricAuthenticationSucceeded();
        void onBiometricAuthenticationFailed(String message);
    } // end of BiometricCallback interface
} // end of BiometricHelper class
