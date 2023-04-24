package com.hhvvg.launcher.model;

import android.content.ComponentName;
import android.content.pm.LauncherActivityInfo;
import android.os.UserHandle;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.AppFilter;
import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

public class AllAppsList extends LauncherComponent {

    @LauncherMethod(inject = Inject.After)
    public void override_findAppInfo(XC_MethodHook.MethodHookParam param, ComponentName cn, UserHandle user) {
        if (AppFilter.getFilteredComponents().contains(cn)) {
            param.setResult(null);
        }
    }

    @LauncherMethod(inject = Inject.After)
    public void override_findActivity(XC_MethodHook.MethodHookParam param, List<LauncherActivityInfo> apps, ComponentName cn) {
        if (AppFilter.getFilteredComponents().contains(cn)) {
            param.setResult(false);
        }
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.model.AllAppsList";
    }
}
