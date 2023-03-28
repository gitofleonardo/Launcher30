package com.hhvvg.launcher.model;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

public class LauncherAppState extends LauncherComponent {

    public void refreshAndReloadLauncher() {
        XposedHelpers.callMethod(getInstance(), "refreshAndReloadLauncher");
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.LauncherAppState";
    }
}
