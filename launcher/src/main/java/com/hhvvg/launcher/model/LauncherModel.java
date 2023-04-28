package com.hhvvg.launcher.model;

import android.os.UserHandle;

import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.LauncherModel")
public class LauncherModel extends Component {
    private LauncherAppState mApp;

    public void onPackageChanged(String pkg, UserHandle user) {
        XposedHelpers.callMethod(getInstance(), "onPackageChanged", pkg, user);
    }

    public void forceReload() {
        XposedHelpers.callMethod(getInstance(), "forceReload");
    }

    public LauncherAppState getApp() {
        if (mApp == null) {
            mApp = new LauncherAppState();
            mApp.setInstance(XposedHelpers.getObjectField(getInstance(), "mApp"));
        }
        return mApp;
    }
}
