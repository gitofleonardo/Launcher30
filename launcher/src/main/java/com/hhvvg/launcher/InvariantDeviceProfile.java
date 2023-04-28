package com.hhvvg.launcher;

import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.service.LauncherService;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.InvariantDeviceProfile")
public class InvariantDeviceProfile extends Component {

    public void onConfigChanged(Object context) {
        XposedHelpers.callMethod(getInstance(), "onConfigChanged", context);
    }

    @LauncherComponent(className = "com.android.launcher3.InvariantDeviceProfile$GridOption")
    public static final class GridOption extends Component {

        @LauncherMethod
        public void $constructor(XC_MethodHook.MethodHookParam param, Context context,
                                AttributeSet attrs, int deviceType) throws RemoteException {
            if (!LauncherService.getLauncherService().isQsbEnable()) {
                XposedHelpers.setIntField(getInstance(), "numSearchContainerColumns", 0);
            }
        }
    }
}
