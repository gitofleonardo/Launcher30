package com.hhvvg.launcher;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherArgs;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.dragndrop.DragObject;
import com.hhvvg.launcher.utils.Logger;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class Workspace extends LauncherComponent {
    public static final String CLASS = "com.android.launcher3.Workspace";

    @LauncherMethod(inject = Inject.After)
    public void override_checkDragObjectIsOverNeighbourPages(XC_MethodHook.MethodHookParam param,
                                                             @LauncherArgs(className = DragObject.CLASS) DragObject d,
                                                             float centerX) {
        if (param.getResult() != null) {
            return;
        }

        if (isPageInTransition()) {
            return;
        }

        float touchX = d.getX();

        int nextPage = getNextPage();
        Object cellLayout = getChildAt(nextPage);

        int pageLeft = (int) XposedHelpers.callMethod(cellLayout, "getLeft");
        int pageRight = (int) XposedHelpers.callMethod(cellLayout, "getRight");

        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        int edgeSlop = viewConfiguration.getScaledEdgeSlop();

        boolean snapLeft = pageLeft + edgeSlop > touchX;
        boolean snapRight = pageRight - edgeSlop < touchX;

        Log.d("Workspace", "touchX=" + touchX + ", left edge=" + (pageLeft +edgeSlop) + ", right edge=" + (pageRight - edgeSlop));

        Object resultCellLayout = null;
        if (snapLeft) {
            resultCellLayout = getChildAt(nextPage - 1);
        }
        if (snapRight) {
            resultCellLayout = getChildAt(nextPage + (isTwoPanelEnabled() ? 2 : 1));
        }

        param.setResult(resultCellLayout);
    }

    private Object getChildAt(int index) {
        return XposedHelpers.callMethod(getInstance(), "getChildAt", index);
    }

    private boolean isTwoPanelEnabled() {
        return (boolean) XposedHelpers.callMethod(getInstance(), "isTwoPanelEnabled");
    }

    private int getNextPage() {
        return (int) XposedHelpers.callMethod(getInstance(), "getNextPage");
    }

    private boolean isPageInTransition() {
        return (boolean) XposedHelpers.callMethod(getInstance(), "isPageInTransition");
    }

    @NonNull
    @Override
    public String getClassName() {
        return CLASS;
    }

    public void invalidate() {
        ((View) getInstance()).invalidate();
    }

    public View getQsb() {
        return (View) XposedHelpers.getObjectField(getInstance(), "mQsb");
    }

    private Context getContext() {
        return (Context) XposedHelpers.callMethod(getInstance(), "getContext");
    }
}
