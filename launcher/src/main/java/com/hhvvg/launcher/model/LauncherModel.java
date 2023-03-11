package com.hhvvg.launcher.model;

import android.os.UserHandle;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

public class LauncherModel extends LauncherComponent {

    public void onPackageChanged(String pkg, UserHandle user) {
        XposedHelpers.callMethod(getInstance(), "onPackageChanged", pkg, user);
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.LauncherModel";
    }
}
