package com.hhvvg.launcher;

import android.app.AndroidAppHelper;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.icon.FastBitmapDrawable;
import com.hhvvg.launcher.model.LauncherModel;
import com.hhvvg.launcher.service.LauncherService;
import com.hhvvg.launcher.utils.ExtensionsKt;
import com.hhvvg.launcher.utils.Logger;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class Launcher extends LauncherComponent {

    private LauncherModel mModel;

    private ILauncherService mLauncherService;

    @LauncherMethod(inject = Inject.After)
    public void override_onCreate(XC_MethodHook.MethodHookParam param, Bundle bundle) {
        initLauncherService();
        LauncherApplication.setLauncher(this);
        getLauncher().setInstance(param.thisObject);

        mModel = createLauncherModel();
    }

    @LauncherMethod(inject = Inject.After)
    public void override_onDestroy(XC_MethodHook.MethodHookParam param) {
        destroyLauncherService();
    }

    protected Context getContext() {
        return (Context) getLauncher().getInstance();
    }

    private LauncherModel createLauncherModel() {
        LauncherModel model = new LauncherModel();
        model.setInstance(XposedHelpers.callMethod(getInstance(), "getModel"));
        return model;
    }

    public LauncherModel getModel() {
        return mModel;
    }

    public static Launcher getLauncher() {
        return ExtensionsKt.getLauncher(AndroidAppHelper.currentApplication());
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.Launcher";
    }

    private void initLauncherService() {
        mLauncherService = LauncherService.getLauncherService();
        try {
            mLauncherService.registerLauncherCallbacks(mCallbacks);
            initConfigurations(mLauncherService);
        } catch (Exception e) {
            Logger.log("Error registering callbacks service", e);
        }
    }

    private void destroyLauncherService() {
        try {
            mLauncherService.registerLauncherCallbacks(mCallbacks);
        } catch (Exception e) {
            Logger.log("Error unregistering callbacks service", e);
        }
    }

    private void initConfigurations(ILauncherService service) throws RemoteException {
        FastBitmapDrawable.CLICK_EFFECT_ENABLE = service.isClickEffectEnable();
    }

    private final ILauncherCallbacks mCallbacks = new ILauncherCallbacks.Stub() {
        @Override
        public void onIconClickEffectEnable(boolean enabled) throws RemoteException {
            FastBitmapDrawable.CLICK_EFFECT_ENABLE = enabled;
        }

        @Override
        public void onComponentLabelUpdated(ComponentName cn, UserHandle user, CharSequence label) throws RemoteException {
            mModel.onPackageChanged(cn.getPackageName(), user);
        }
    };
}
