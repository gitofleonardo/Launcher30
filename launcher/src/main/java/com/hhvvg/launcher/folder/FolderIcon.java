package com.hhvvg.launcher.folder;

import android.graphics.Canvas;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.component.ViewGroupComponent;
import com.hhvvg.launcher.icon.DrawParams;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.folder.FolderIcon")
public class FolderIcon extends ViewGroupComponent {

    @LauncherMethod
    public void before$drawDot(XC_MethodHook.MethodHookParam param, Canvas canvas) {
        Object dotParams = XposedHelpers.getObjectField(getInstance(), "mDotParams");
        XposedHelpers.setAdditionalInstanceField(dotParams, DrawParams.EXTRA_NOTIFICATION_COUNT, getNotificationCount());
    }

    private int getNotificationCount() {
        Object dotInfo = XposedHelpers.getObjectField(getInstance(), "mDotInfo");
        if (dotInfo == null) {
            return 0;
        }
        return XposedHelpers.getIntField(dotInfo, "mNumNotifications");
    }
}
