package com.hhvvg.launcher;

import android.content.res.Resources;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.service.LauncherService;
import com.hhvvg.launcher.utils.Logger;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class DeviceProfile extends LauncherComponent {
    private ILauncherService mService;

    private ILauncherService getService() {
        if (mService == null) {
            mService = LauncherService.getLauncherService();
        }
        return mService;
    }

    @LauncherMethod(inject = Inject.After)
    public void override_updateIconSize(XC_MethodHook.MethodHookParam param, float scale, Resources resources) throws RemoteException {
        if (!getService().isIconTextVisible()) {
            setIconTextSize(0);
        }
    }

    private void setIconTextSize(int sizePx) {
        XposedHelpers.setIntField(getInstance(), "iconTextSizePx", sizePx);
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.DeviceProfile";
    }
}
