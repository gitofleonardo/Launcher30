package com.hhvvg.launcher.icon;

import android.graphics.Canvas;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.component.MethodInjection;
import com.hhvvg.launcher.component.ViewComponent;

import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.BubbleTextView")
public class BubbleTextView extends ViewComponent {
    public static Integer sDotParamsColor = null;

    @LauncherMethod(injections = MethodInjection.Before)
    public void drawDotIfNecessary(Canvas canvas) {
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
}
