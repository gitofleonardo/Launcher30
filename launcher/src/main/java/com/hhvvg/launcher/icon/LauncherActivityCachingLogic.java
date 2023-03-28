package com.hhvvg.launcher.icon;

import android.content.pm.LauncherActivityInfo;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.ILauncherService;
import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.service.LauncherService;

import de.robv.android.xposed.XC_MethodHook;

public class LauncherActivityCachingLogic extends LauncherComponent {
    private ILauncherService mService;

    private ILauncherService getService() {
        if (mService == null) {
            mService = LauncherService.getLauncherService();
        }
        return mService;
    }

    @LauncherMethod(inject = Inject.After)
    public void override_getLabel(XC_MethodHook.MethodHookParam param, LauncherActivityInfo info) throws RemoteException {
        CharSequence label = getService().getComponentLabel(info.getComponentName(), info.getUser());
        if (label != null) {
            param.setResult(label.toString());
        }
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.icons.LauncherActivityCachingLogic";
    }
}
