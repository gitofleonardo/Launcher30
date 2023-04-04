package com.hhvvg.launcher;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

public class InvariantDeviceProfile extends LauncherComponent {
    private static final String CLASS = "com.android.launcher3.InvariantDeviceProfile";

    public void onConfigChanged(Object context) {
        XposedHelpers.callMethod(getInstance(), "onConfigChanged", context);
    }

    @NonNull
    @Override
    public String getClassName() {
        return CLASS;
    }
}
