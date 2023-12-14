package com.example.gradetrackerapp.authentication;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.security.Key;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BiometricHelper {
    private static final int AUTHENTICATION_REQUEST_CODE = 123;
    private boolean authenticationFailed = false;
    private final Context context;
    private BiometricCallback biometricCallback;
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
                if (isDeviceSecure()) {
                    showPasswordAuthentication();
                } else {
                    biometricCallback.onBiometricAuthenticationFailed("Biometric not enrolled, and device is not secure.");
                }
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                biometricCallback.onBiometricAuthenticationFailed("Biometric authentication not available.");
                break;
        }
    } // end of initiateBiometricAuthentication

    private boolean isDeviceSecure() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            return keyguardManager != null && keyguardManager.isDeviceSecure();
        }
        return false;
    } // end of isDeviceSecure

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

    private void showPasswordAuthentication() {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        if (keyguardManager != null && keyguardManager.isKeyguardSecure()) {
            Intent authIntent = keyguardManager.createConfirmDeviceCredentialIntent("Authenticate", "Confirm your device password");
            if (authIntent != null) {
                ((Activity) context).startActivityForResult(authIntent, AUTHENTICATION_REQUEST_CODE);
            }
        } else {
            showToast("Device is not secure, unable to use device password.");
        }
    }

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
