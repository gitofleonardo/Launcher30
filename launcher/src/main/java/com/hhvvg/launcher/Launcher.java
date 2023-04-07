package com.hhvvg.launcher;

import android.app.AndroidAppHelper;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.UserHandle;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.icon.BubbleTextView;
import com.hhvvg.launcher.icon.DotRenderer;
import com.hhvvg.launcher.icon.FastBitmapDrawable;
import com.hhvvg.launcher.icon.LauncherIconProvider;
import com.hhvvg.launcher.icon.Workspace;
import com.hhvvg.launcher.model.LauncherModel;
import com.hhvvg.launcher.service.LauncherService;
import com.hhvvg.launcher.utils.Executors;
import com.hhvvg.launcher.utils.ExtensionsKt;
import com.hhvvg.launcher.utils.Logger;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class Launcher extends LauncherComponent {

    private LauncherModel mModel;
    private Workspace mWorkspace;

    private ILauncherService mLauncherService;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @LauncherMethod(inject = Inject.After)
    public void override_onCreate(XC_MethodHook.MethodHookParam param, Bundle bundle) {
        initLauncherService();
        LauncherApplication.setLauncher(this);
        getLauncher().setInstance(param.thisObject);

        mModel = createLauncherModel();
        mWorkspace = createWorkspace();
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

    private Workspace createWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setInstance(XposedHelpers.callMethod(getInstance(), "getWorkspace"));
        return workspace;
    }

    public LauncherModel getModel() {
        return mModel;
    }

    public Workspace getWorkspace() {
        return mWorkspace;
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
        FastBitmapDrawable.sClickEffectEnable = service.isClickEffectEnable();
        BubbleTextView.sDotParamsColor = service.getDotParamsColor();
        LauncherIconProvider.sIconProvider = service.getIconPackProvider();
        DotRenderer.sDrawNotificationCount = service.isDrawNotificationCount();
        CellLayout.sHideSpringLoadedBg = !service.isSpringLoadedBgEnable();
        CellLayout.sEnableQSB = service.isQsbEnable();
    }

    public void onIdpChanged(boolean modelPropertiesChanged) {
        XposedHelpers.callMethod(getInstance(), "onIdpChanged", modelPropertiesChanged);
    }

    private final ILauncherCallbacks mCallbacks = new ILauncherCallbacks.Stub() {
        @Override
        public void onIconClickEffectEnable(boolean enabled) throws RemoteException {
            FastBitmapDrawable.sClickEffectEnable = enabled;
        }

        @Override
        public void onComponentLabelUpdated(ComponentName cn, UserHandle user, CharSequence label) throws RemoteException {
            getModel().onPackageChanged(cn.getPackageName(), user);
        }

        @Override
        public void onDotParamsColorChanged(int color) throws RemoteException {
            BubbleTextView.sDotParamsColor = color;
            getWorkspace().invalidate();
        }

        @Override
        public void onDotParamsColorRestored() {
            BubbleTextView.sDotParamsColor = null;
            getWorkspace().invalidate();
        }

        @Override
        public void onIconPackProviderChanged(String providerPkg) throws RemoteException {
            try {
                LauncherIconProvider.sIconProvider = providerPkg;
                LauncherIconProvider.sIconCaches.clear();
                getModel().getApp().refreshAndReloadLauncher();
            } catch (Exception e) {
                Logger.log("error", e);
            }
        }

        @Override
        public void onLauncherReload() {
            getModel().forceReload();
        }

        @Override
        public void onIconTextVisibilityChanged(boolean visible) {
            Executors.postUiThread(() -> {
                getModel().getApp().getIdp().onConfigChanged(getInstance());
                onIdpChanged(true);
            });
        }

        @Override
        public void onDrawNotificationCountChanged(boolean enable) throws RemoteException {
            DotRenderer.sDrawNotificationCount = enable;
            getModel().forceReload();
        }

        @Override
        public void onSpringLoadedBgEnable(boolean enable) throws RemoteException {
            CellLayout.sHideSpringLoadedBg = !enable;
        }

        @Override
        public void onQsbStateChanged(boolean enable) {
            CellLayout.sEnableQSB = enable;
            getModel().forceReload();
        }
    };
}
