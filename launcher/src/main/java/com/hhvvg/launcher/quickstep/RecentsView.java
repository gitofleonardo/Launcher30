package com.hhvvg.launcher.quickstep;

import android.content.ComponentName;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.hhvvg.launcher.ILauncherService;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.component.ViewGroupComponent;
import com.hhvvg.launcher.service.LauncherService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.quickstep.views.RecentsView")
public class RecentsView extends ViewGroupComponent {
    private final ILauncherService mService = LauncherService.getLauncherService();

    @LauncherMethod
    public void before$applyLoadPlan(XC_MethodHook.MethodHookParam param, ArrayList tasks) throws RemoteException {
        if (!mService.isPrivacyHiddenFromRecents()) {
            return;
        }
        Iterator iter = tasks.iterator();
        while (iter.hasNext()) {
            Object task = iter.next();
            if (isTargetTaskHidden(task)) {
                iter.remove();
            }
        }
    }

    private boolean isTargetTaskHidden(Object groupTask) throws RemoteException {
        ComponentName task1 = task1Component(groupTask);
        ComponentName task2 = task2Pkg(groupTask);

        Set<ComponentName> privacyComponents = Set.copyOf(mService.getPrivacyItems());
        return privacyComponents.contains(task1) || privacyComponents.contains(task2);
    }

    private static ComponentName task1Component(Object groupTask) {
        Object task1 = XposedHelpers.getObjectField(groupTask, "task1");
        Object task1Key = XposedHelpers.getObjectField(task1, "key");
        return (ComponentName) XposedHelpers.callMethod(task1Key, "getComponent");
    }

    @Nullable
    private static ComponentName task2Pkg(Object groupTask) {
        Object task2 = XposedHelpers.getObjectField(groupTask, "task2");
        if (task2 == null) {
            return null;
        }
        Object task2Key = XposedHelpers.getObjectField(task2, "key");
        return (ComponentName) XposedHelpers.callMethod(task2Key, "getComponent");
    }
}
