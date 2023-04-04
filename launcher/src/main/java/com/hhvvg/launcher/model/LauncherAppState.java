package com.hhvvg.launcher.model;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.InvariantDeviceProfile;
import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

public class LauncherAppState extends LauncherComponent {

    public void refreshAndReloadLauncher() {
        XposedHelpers.callMethod(getInstance(), "refreshAndReloadLauncher");
    }

    public InvariantDeviceProfile getIdp() {
        InvariantDeviceProfile idp = new InvariantDeviceProfile();
        Object realIdp = XposedHelpers.callMethod(getInstance(), "getInvariantDeviceProfile");
        idp.setInstance(realIdp);
        return idp;
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.LauncherAppState";
    }
}
