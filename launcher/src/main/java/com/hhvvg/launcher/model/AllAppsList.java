package com.hhvvg.launcher.model;

import android.content.ComponentName;
import android.content.pm.LauncherActivityInfo;
import android.os.UserHandle;

import com.hhvvg.launcher.AppFilter;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

@LauncherComponent(className = "com.android.launcher3.model.AllAppsList")
public class AllAppsList extends Component {

    @LauncherMethod
    public void $findAppInfo(XC_MethodHook.MethodHookParam param, ComponentName cn, UserHandle user) {
        if (AppFilter.getFilteredComponents().contains(cn)) {
            param.setResult(null);
        }
    }

    @LauncherMethod
    public void $findActivity(XC_MethodHook.MethodHookParam param, List<LauncherActivityInfo> apps, ComponentName cn) {
        if (AppFilter.getFilteredComponents().contains(cn)) {
            param.setResult(false);
        }
    }
}
