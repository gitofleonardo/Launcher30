package com.hhvvg.launcher;

import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.service.LauncherService;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class InvariantDeviceProfile extends LauncherComponent {
    private static final String CLASS = "com.android.launcher3.InvariantDeviceProfile";

    public void onConfigChanged(Object context) {
        XposedHelpers.callMethod(getInstance(), "onConfigChanged", context);
    }

    @NonNull
    @Override
    public String getClassName() {
        return CLASS;
    }

    public static final class GridOption extends LauncherComponent {

        @LauncherMethod(inject = Inject.After)
        public void constructor(XC_MethodHook.MethodHookParam param, Context context,
                                AttributeSet attrs, int deviceType) throws RemoteException {
            if (!LauncherService.getLauncherService().isQsbEnable()) {
                XposedHelpers.setIntField(getInstance(), "numSearchContainerColumns", 0);
            }
        }

        @NonNull
        @Override
        public String getClassName() {
            return "com.android.launcher3.InvariantDeviceProfile$GridOption";
        }
    }
}
