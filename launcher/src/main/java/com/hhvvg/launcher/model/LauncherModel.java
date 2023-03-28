package com.hhvvg.launcher.model;

import android.os.UserHandle;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

public class LauncherModel extends LauncherComponent {
    private LauncherAppState mApp;

    public void onPackageChanged(String pkg, UserHandle user) {
        XposedHelpers.callMethod(getInstance(), "onPackageChanged", pkg, user);
    }

    public void forceReload() {
        XposedHelpers.callMethod(getInstance(), "forceReload");
    }

    public LauncherAppState getApp() {
        if (mApp == null) {
            mApp = new LauncherAppState();
            mApp.setInstance(XposedHelpers.getObjectField(getInstance(), "mApp"));
        }
        return mApp;
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.LauncherModel";
    }
}
