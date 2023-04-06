package com.hhvvg.launcher.icon;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.utils.Logger;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class BubbleTextView extends LauncherComponent {
    public static Integer sDotParamsColor = null;

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.BubbleTextView";
    }

    @LauncherMethod(inject = Inject.Before)
    public void override_drawDotIfNecessary(XC_MethodHook.MethodHookParam param, Canvas canvas) {
        if (sDotParamsColor != null) {
            setDotParamsColor(sDotParamsColor);
        }
        Object dotParams = XposedHelpers.getObjectField(getInstance(), "mDotParams");
        XposedHelpers.setAdditionalInstanceField(dotParams, DrawParams.EXTRA_NOTIFICATION_COUNT, getNotificationCount());
    }

    private int getNotificationCount() {
        Object dotInfo = XposedHelpers.getObjectField(getInstance(), "mDotInfo");
        if (dotInfo == null) {
            return 0;
        }
        return XposedHelpers.getIntField(dotInfo, "mTotalCount");
    }

    private void setDotParamsColor(int color) {
        Object dotParams = getDotParams();
        XposedHelpers.setIntField(dotParams, "dotColor", color);
    }


    private Object getDotParams() {
        return XposedHelpers.getObjectField(getInstance(), "mDotParams");
    }

    public void invalidate() {
        ((View) getInstance()).invalidate();
    }
}
