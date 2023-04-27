package com.hhvvg.launcher.states;

import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.ILauncherService;
import com.hhvvg.launcher.Launcher;
import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherArgs;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.service.LauncherService;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class SpringLoadedState extends LauncherComponent {

    private ILauncherService mService;

    private ILauncherService getService() {
        if (mService == null) {
            mService = LauncherService.getLauncherService();
        }
        return mService;
    }

    @LauncherMethod(inject = Inject.After)
    public void override_getHotseatScaleAndTranslation(XC_MethodHook.MethodHookParam param,
                                                       @LauncherArgs(className = Launcher.CLASS) Launcher launcher) throws RemoteException {
        if (!getService().isUseCustomSpringLoadedEffect()) {
            return;
        }

        Object scaleAndTranslation = param.getResult();
        Object workspaceScaleAndTranslation = getWorkspaceScaleAndTranslation(launcher);

        float workspaceTranY = XposedHelpers.getFloatField(workspaceScaleAndTranslation, "translationY");

        XposedHelpers.setFloatField(scaleAndTranslation, "translationY", workspaceTranY);
    }

    @LauncherMethod(inject = Inject.After)
    public void override_getWorkspaceScaleAndTranslation(XC_MethodHook.MethodHookParam param,
                                                         @LauncherArgs(className = Launcher.CLASS) Launcher launcher) throws RemoteException {
        if (!getService().isUseCustomSpringLoadedEffect()) {
            return;
        }
        Object scaleAndTran = param.getResult();
        XposedHelpers.setFloatField(scaleAndTran, "scale", 1f);
    }

    private Object getWorkspaceScaleAndTranslation(Launcher launcher) {
        return XposedHelpers.callMethod(getInstance(), "getWorkspaceScaleAndTranslation", launcher.getInstance());
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.states.SpringLoadedState";
    }
}
