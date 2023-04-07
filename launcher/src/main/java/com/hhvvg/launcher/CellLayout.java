package com.hhvvg.launcher;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherArgs;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.icon.Workspace;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class CellLayout extends LauncherComponent {
    public static boolean sHideSpringLoadedBg = true;
    public static boolean sEnableQSB = false;

    @LauncherMethod(inject = Inject.After)
    public void override_updateBgAlpha(XC_MethodHook.MethodHookParam param) {
        if (sHideSpringLoadedBg) {
            getBackground().setAlpha(0);
        }
    }

    @LauncherMethod(inject = Inject.Before)
    public void override_addViewToCellLayout(XC_MethodHook.MethodHookParam hookParam, View child, int index, int childId,
                                             @LauncherArgs(className = LayoutParams.CLASS) LayoutParams params,
                                             boolean markCells) {
        if (!sEnableQSB && getWorkspace() != null && getWorkspace().getQsb() == child) {
            params.setCellX(-1);
        }
    }

    @Nullable
    private Workspace getWorkspace() {
        Object parent = XposedHelpers.callMethod(getInstance(), "getParent");
        Class<?> workspaceClass = XposedHelpers.findClass(Workspace.CLASS, Init.classLoader);
        if (workspaceClass.isAssignableFrom(parent.getClass())) {
            Workspace w = new Workspace();
            w.setInstance(parent);
            return w;
        }
        return null;
    }

    private Drawable getBackground() {
        return (Drawable) XposedHelpers.getObjectField(getInstance(), "mBackground");
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.CellLayout";
    }

    public static class LayoutParams extends LauncherComponent {

        public static final String CLASS = "com.android.launcher3.CellLayout$LayoutParams";

        public void setCellX(int cellX) {
            XposedHelpers.setIntField(getInstance(), "cellX", cellX);
        }

        @NonNull
        @Override
        public String getClassName() {
            return CLASS;
        }
    }
}
