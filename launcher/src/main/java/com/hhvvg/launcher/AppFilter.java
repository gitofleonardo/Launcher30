package com.hhvvg.launcher;

import android.content.ComponentName;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.utils.Logger;

import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;

public class AppFilter extends LauncherComponent {
    private static final String CLASS = "com.android.launcher3.AppFilter";
    private static final Set<ComponentName> sFilteredComponents = new HashSet<>();

    @LauncherMethod(inject = Inject.After)
    public void override_shouldShowApp(XC_MethodHook.MethodHookParam param, ComponentName cn) {
        if (sFilteredComponents.contains(cn)) {
            param.setResult(false);
        }
    }

    public static void updateGlobalFilteredComponents(Set<ComponentName> components) {
        sFilteredComponents.clear();
        sFilteredComponents.addAll(components);
    }

    public static Set<ComponentName> getFilteredComponents() {
        return sFilteredComponents;
    }

    @NonNull
    @Override
    public String getClassName() {
        return CLASS;
    }
}
