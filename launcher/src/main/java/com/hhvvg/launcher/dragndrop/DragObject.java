package com.hhvvg.launcher.dragndrop;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

public class DragObject extends LauncherComponent {
    public static final String CLASS = "com.android.launcher3.DropTarget$DragObject";

    public float getY() {
        return XposedHelpers.getFloatField(getInstance(), "y");
    }

    public float getX() {
        return XposedHelpers.getFloatField(getInstance(), "x");
    }

    @NonNull
    @Override
    public String getClassName() {
        return CLASS;
    }
}
