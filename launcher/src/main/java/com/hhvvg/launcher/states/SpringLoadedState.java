package com.hhvvg.launcher.states;

import android.os.RemoteException;

import com.hhvvg.launcher.ILauncherService;
import com.hhvvg.launcher.Launcher;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.service.LauncherService;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.states.SpringLoadedState")
public class SpringLoadedState extends Component {

    private ILauncherService mService;

    private ILauncherService getService() {
        if (mService == null) {
            mService = LauncherService.getLauncherService();
        }
        return mService;
    }

    @LauncherMethod
    public void $getHotseatScaleAndTranslation(XC_MethodHook.MethodHookParam param,
                                                       Launcher launcher) throws RemoteException {
        if (!getService().isUseCustomSpringLoadedEffect()) {
            return;
        }

        Object scaleAndTranslation = param.getResult();
        Object workspaceScaleAndTranslation = getWorkspaceScaleAndTranslation(launcher);

        float workspaceTranY = XposedHelpers.getFloatField(workspaceScaleAndTranslation, "translationY");

        XposedHelpers.setFloatField(scaleAndTranslation, "translationY", workspaceTranY);
    }

    @LauncherMethod
    public void $getWorkspaceScaleAndTranslation(XC_MethodHook.MethodHookParam param,
                                                         Launcher launcher) throws RemoteException {
        if (!getService().isUseCustomSpringLoadedEffect()) {
            return;
        }
        Object scaleAndTran = param.getResult();
        XposedHelpers.setFloatField(scaleAndTran, "scale", 1f);
    }

    private Object getWorkspaceScaleAndTranslation(Launcher launcher) {
        return XposedHelpers.callMethod(getInstance(), "getWorkspaceScaleAndTranslation", launcher.getInstance());
    }
}
