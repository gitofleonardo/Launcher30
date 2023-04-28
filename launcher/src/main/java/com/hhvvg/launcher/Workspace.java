package com.hhvvg.launcher;

import android.view.View;
import android.view.ViewConfiguration;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.component.ViewGroupComponent;
import com.hhvvg.launcher.dragndrop.DragObject;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.Workspace")
public class Workspace extends ViewGroupComponent {

    @LauncherMethod
    public void $checkDragObjectIsOverNeighbourPages(XC_MethodHook.MethodHookParam param,
                                                             DragObject d, float centerX) {
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

        Object resultCellLayout = null;
        if (snapLeft) {
            resultCellLayout = getChildAt(nextPage - 1);
        }
        if (snapRight) {
            resultCellLayout = getChildAt(nextPage + (isTwoPanelEnabled() ? 2 : 1));
        }

        param.setResult(resultCellLayout);
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

    public View getQsb() {
        return (View) XposedHelpers.getObjectField(getInstance(), "mQsb");
    }
}
