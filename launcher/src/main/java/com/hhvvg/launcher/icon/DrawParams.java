package com.hhvvg.launcher.icon;

import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

public class DrawParams extends LauncherComponent {
    public static final String EXTRA_NOTIFICATION_COUNT = "_extra_notification_total_count";

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

    public int getNotificationCount() {
        Object count = XposedHelpers.getAdditionalInstanceField(getInstance(), EXTRA_NOTIFICATION_COUNT);
        if (!(count instanceof Integer)) {
            return 0;
        }
        return (Integer) count;
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.icons.DotRenderer$DrawParams";
    }
}
