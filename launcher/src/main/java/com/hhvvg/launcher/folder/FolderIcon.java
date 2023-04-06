package com.hhvvg.launcher.folder;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.icon.DrawParams;
import com.hhvvg.launcher.utils.Logger;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class FolderIcon extends LauncherComponent {

    @LauncherMethod(inject = Inject.Before)
    public void override_drawDot(XC_MethodHook.MethodHookParam param, Canvas canvas) {
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

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.folder.FolderIcon";
    }
}
