package com.hhvvg.launcher.icon;

import android.view.View;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

public class Workspace extends LauncherComponent {
    public static final String CLASS = "com.android.launcher3.Workspace";

    @NonNull
    @Override
    public String getClassName() {
        return CLASS;
    }

    public void invalidate() {
        ((View) getInstance()).invalidate();
    }

    public View getQsb() {
        return (View) XposedHelpers.getObjectField(getInstance(), "mQsb");
    }
}
