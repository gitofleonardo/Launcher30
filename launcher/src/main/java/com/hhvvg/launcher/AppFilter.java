package com.hhvvg.launcher;

import android.content.ComponentName;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;

import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;

@LauncherComponent(className = "com.android.launcher3.AppFilter")
public class AppFilter extends Component {
    private static final Set<ComponentName> sFilteredComponents = new HashSet<>();

    @LauncherMethod
    public void shouldShowApp(XC_MethodHook.MethodHookParam param, ComponentName cn) {
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
}
