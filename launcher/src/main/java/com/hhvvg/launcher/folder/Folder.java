package com.hhvvg.launcher.folder;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class Folder extends LauncherComponent {
    public static boolean sCenterOpenedFolder = true;

    @LauncherMethod(inject = Inject.After)
    public void override_centerAboutIcon(XC_MethodHook.MethodHookParam param) {
        if (sCenterOpenedFolder) {
            centerAboutDragLayer();
        }
    }

    private void centerAboutDragLayer() {
        ViewGroup dragLayer = getDragLayer();
        Object lp = getLayoutParams();
        int width = getFolderWidth();
        int height = getFolderHeight();

        int left = (dragLayer.getWidth() - width) / 2;
        XposedHelpers.setIntField(lp, "x", left);

        int top = (dragLayer.getHeight() - height) / 2;
        XposedHelpers.setIntField(lp, "y", top);
    }

    private int getFolderHeight() {
        return (int) XposedHelpers.callMethod(getInstance(), "getFolderHeight");
    }

    private int getFolderWidth() {
        return (int) XposedHelpers.callMethod(getInstance(), "getFolderWidth");
    }

    private Object getLayoutParams() {
        return XposedHelpers.callMethod(getInstance(), "getLayoutParams");
    }

    private Object getActivityContext() {
        return XposedHelpers.getObjectField(getInstance(), "mActivityContext");
    }

    private ViewGroup getDragLayer() {
        return (ViewGroup) XposedHelpers.callMethod(getActivityContext(), "getDragLayer");
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.folder.Folder";
    }
}
