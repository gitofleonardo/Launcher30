package com.hhvvg.launcher.icon;

import android.view.View;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.LauncherComponent;

public class Workspace extends LauncherComponent {
    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.Workspace";
    }

    public void invalidate() {
        ((View) getInstance()).invalidate();
    }
}
