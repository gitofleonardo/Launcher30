package com.hhvvg.launcher.icon;

import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

public class DrawParams extends LauncherComponent {

    public int getDotColor() {
        return XposedHelpers.getIntField(getInstance(), "dotColor");
    }

    public Rect getIconBounds() {
        return (Rect) XposedHelpers.getObjectField(getInstance(), "iconBounds");
    }

    public float getScale() {
        return XposedHelpers.getFloatField(getInstance(), "scale");
    }

    public boolean isLeftAlign() {
        return XposedHelpers.getBooleanField(getInstance(), "leftAlign");
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.icons.DotRenderer$DrawParams";
    }
}
