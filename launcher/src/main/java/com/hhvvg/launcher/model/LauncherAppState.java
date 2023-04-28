package com.hhvvg.launcher.model;

import com.hhvvg.launcher.InvariantDeviceProfile;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.LauncherAppState")
public class LauncherAppState extends Component {

    public void refreshAndReloadLauncher() {
        XposedHelpers.callMethod(getInstance(), "refreshAndReloadLauncher");
    }

    public InvariantDeviceProfile getIdp() {
        InvariantDeviceProfile idp = new InvariantDeviceProfile();
        Object realIdp = XposedHelpers.callMethod(getInstance(), "getInvariantDeviceProfile");
        idp.setInstance(realIdp);
        return idp;
    }
}
