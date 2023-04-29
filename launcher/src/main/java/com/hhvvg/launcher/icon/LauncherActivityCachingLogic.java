package com.hhvvg.launcher.icon;

import android.content.Context;
import android.content.pm.LauncherActivityInfo;
import android.os.RemoteException;

import com.hhvvg.launcher.ILauncherService;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.service.LauncherService;

import de.robv.android.xposed.XC_MethodHook;

@LauncherComponent(className = "com.android.launcher3.icons.LauncherActivityCachingLogic")
public class LauncherActivityCachingLogic extends Component {
    private ILauncherService mService;

    private ILauncherService getService() {
        if (mService == null) {
            mService = LauncherService.getLauncherService();
        }
        return mService;
    }

    @LauncherMethod
    public void getLabel(XC_MethodHook.MethodHookParam param, LauncherActivityInfo info) throws RemoteException {
        CharSequence label = getService().getComponentLabel(info.getComponentName(), info.getUser());
        if (label != null) {
            param.setResult(label.toString());
        }
    }

    @LauncherMethod
    public void loadIcon(Context context, LauncherActivityInfo info) {
    }
}
