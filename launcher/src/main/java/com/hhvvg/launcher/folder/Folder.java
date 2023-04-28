package com.hhvvg.launcher.folder;

import android.view.ViewGroup;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.component.ViewGroupComponent;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.folder.Folder")
public class Folder extends ViewGroupComponent {
    public static boolean sCenterOpenedFolder = true;

    @LauncherMethod
    public void $centerAboutIcon(XC_MethodHook.MethodHookParam param) {
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
}
