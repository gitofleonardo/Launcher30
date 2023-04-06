package com.hhvvg.launcher;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class CellLayout extends LauncherComponent {
    public static boolean sHideSpringLoadedBg = true;

    @LauncherMethod(inject = Inject.After)
    public void override_updateBgAlpha(XC_MethodHook.MethodHookParam param) {
        if (sHideSpringLoadedBg) {
            getBackground().setAlpha(0);
        }
    }

    private Drawable getBackground() {
        return (Drawable) XposedHelpers.getObjectField(getInstance(), "mBackground");
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.CellLayout";
    }
}
