package com.hhvvg.launcher;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.Nullable;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.component.ViewGroupComponent;
import com.hhvvg.launcher.hook.HookProviderKt;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.CellLayout")
public class CellLayout extends ViewGroupComponent {
    public static boolean sHideSpringLoadedBg = true;
    public static boolean sEnableQSB = false;

    @LauncherMethod
    public void $updateBgAlpha(XC_MethodHook.MethodHookParam param) {
        if (sHideSpringLoadedBg) {
            getBackground().setAlpha(0);
        }
    }

    @LauncherMethod
    public void before$addViewToCellLayout(XC_MethodHook.MethodHookParam hookParam, View child,
                                           int index, int childId, LayoutParams params, boolean markCells) {
        if (!sEnableQSB && getWorkspace() != null && getWorkspace().getQsb() == child) {
            params.setCellX(-1);
        }
    }

    @Nullable
    private Workspace getWorkspace() {
        Object parent = XposedHelpers.callMethod(getInstance(), "getParent");
        Class<?> workspaceClass = HookProviderKt.getLauncherJavaClass(Workspace.class);
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

    @LauncherComponent(className = "com.android.launcher3.CellLayout$LayoutParams")
    public static class LayoutParams extends Component {
        public void setCellX(int cellX) {
            XposedHelpers.setIntField(getInstance(), "cellX", cellX);
        }
    }
}
