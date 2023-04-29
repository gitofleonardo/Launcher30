package com.hhvvg.launcher;

import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;

import de.robv.android.xposed.XC_MethodHook;

@LauncherComponent(className = "android.app.Application")
public class LauncherApplication extends Component {

    @LauncherMethod
    public void onCreate() {

    }
}
