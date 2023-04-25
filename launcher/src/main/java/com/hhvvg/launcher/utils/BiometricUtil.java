package com.hhvvg.launcher.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.hhvvg.launcher.R;

import java.util.concurrent.Executor;

public class BiometricUtil {

    public static final int BIOMETRIC_NON_CREDENTIAL = BiometricManager.Authenticators.BIOMETRIC_STRONG |
            BiometricManager.Authenticators.BIOMETRIC_WEAK;
    public static final int BIOMETRIC_CREDENTIAL = BiometricManager.Authenticators.DEVICE_CREDENTIAL;

    public static boolean isBiometricAvailable(Context context, int biometricType) {
        BiometricManager manager = BiometricManager.from(context);
        switch (manager.canAuthenticate(biometricType)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                return true;
            default:
                return false;
        }
    }

    public static void showBiometricPrompt(
            Fragment context,
            int biometricType,
            String title,
            String subtitle,
            String negativeText,
            Runnable successCallback,
            Runnable failureCallback
    ) {
        if (!isBiometricAvailable(context.requireContext(), biometricType)) {
            Toast.makeText(context.requireContext(), context.getString(R.string.title_biometric_not_available), Toast.LENGTH_SHORT).show();
            return;
        }
        Executor executor = ContextCompat.getMainExecutor(context.requireContext());
        BiometricPrompt prompt = new BiometricPrompt(context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (failureCallback != null) {
                    failureCallback.run();
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (successCallback != null) {
                    successCallback.run();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                if (failureCallback != null) {
                    failureCallback.run();
                }
            }
        });
        BiometricPrompt.PromptInfo info = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setAllowedAuthenticators(biometricType)
                .setNegativeButtonText(negativeText)
                .build();
        prompt.authenticate(info);
    }

    public static void showBiometricPrompt(
            FragmentActivity context,
            int biometricType,
            String title,
            String subtitle,
            String negativeText,
            Runnable successCallback,
            Runnable failureCallback
    ) {
        if (!isBiometricAvailable(context, biometricType)) {
            Toast.makeText(context, context.getString(R.string.title_biometric_not_available), Toast.LENGTH_SHORT).show();
            return;
        }
        Executor executor = ContextCompat.getMainExecutor(context);
        BiometricPrompt prompt = new BiometricPrompt(context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (failureCallback != null) {
                    failureCallback.run();
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (successCallback != null) {
                    successCallback.run();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                if (failureCallback != null) {
                    failureCallback.run();
                }
            }
        });
        BiometricPrompt.PromptInfo info = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setAllowedAuthenticators(biometricType)
                .setNegativeButtonText(negativeText)
                .build();
        prompt.authenticate(info);
    }
}
