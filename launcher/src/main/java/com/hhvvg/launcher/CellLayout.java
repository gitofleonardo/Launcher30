package com.hhvvg.launcher;

import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.view.View;

import androidx.annotation.Nullable;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.component.MethodInjection;
import com.hhvvg.launcher.component.ViewGroupComponent;
import com.hhvvg.launcher.hook.HookProviderKt;
import com.hhvvg.launcher.service.LauncherService;

import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.CellLayout")
public class CellLayout extends ViewGroupComponent {
    public static volatile boolean sHideSpringLoadedBg = true;

    @LauncherMethod
    public void updateBgAlpha() {
        if (sHideSpringLoadedBg) {
            getBackground().setAlpha(0);
        }
    }

    @LauncherMethod(injections = MethodInjection.Before)
    public void addViewToCellLayout(View child, int index, int childId, LayoutParams params,
                                           boolean markCells) throws RemoteException {
        if (!LauncherService.getLauncherService().isQsbEnable() && getWorkspace() != null &&
                getWorkspace().getQsb() == child) {
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
