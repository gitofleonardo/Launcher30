package com.hhvvg.launcher;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.LauncherComponent;

public class DeviceProfile extends LauncherComponent {
    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.DeviceProfile";
    }
}
