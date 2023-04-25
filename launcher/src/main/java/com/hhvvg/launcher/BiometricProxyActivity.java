package com.hhvvg.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hhvvg.launcher.utils.BiometricUtil;

public class BiometricProxyActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            String targetAction = getIntent().getData().getQueryParameter("target");
            boolean allowDirectStart = getIntent().getData().getBooleanQueryParameter("allowDirectStart", false);
            if (!BiometricUtil.isBiometricAvailable(this, BiometricUtil.BIOMETRIC_NON_CREDENTIAL) && allowDirectStart) {
                openActionIntent(targetAction);
                return;
            }
            showPrompt(targetAction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPrompt(String targetAction) {
        BiometricUtil.showBiometricPrompt(
                this,
                BiometricUtil.BIOMETRIC_NON_CREDENTIAL,
                getString(R.string.title_authenticate),
                getString(R.string.subtitle_auth),
                getString(R.string.cancel),
                () -> {
                    openActionIntent(targetAction);
                    finish();
                },
                () -> {
                    Toast.makeText(this, getString(R.string.title_auth_failed), Toast.LENGTH_SHORT).show();
                    finish();
                }
        );
    }

    public void openActionIntent(String action) {
        Intent intent = new Intent(action);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
