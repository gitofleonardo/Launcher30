package com.hhvvg.launcher.icon;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

public class IconNormalizer extends LauncherComponent {

    public float getScale(Drawable d, RectF outBounds, Path path, boolean[] outMaskShape) {
        return (float) XposedHelpers.callMethod(getInstance(), "getScale", d, outBounds,
                path, outMaskShape);
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.icons.IconNormalizer";
    }
}
