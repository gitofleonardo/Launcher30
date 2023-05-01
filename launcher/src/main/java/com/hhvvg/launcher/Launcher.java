package com.hhvvg.launcher;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;

import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.folder.Folder;
import com.hhvvg.launcher.icon.BubbleTextView;
import com.hhvvg.launcher.icon.DotRenderer;
import com.hhvvg.launcher.icon.FastBitmapDrawable;
import com.hhvvg.launcher.icon.LauncherIconProvider;
import com.hhvvg.launcher.model.LauncherModel;
import com.hhvvg.launcher.service.LauncherService;
import com.hhvvg.launcher.utils.Executors;
import com.hhvvg.launcher.utils.Logger;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.Launcher")
public class Launcher extends Component {

    private static ILauncherCallbacks sLauncherCallback;

    private LauncherModel mModel;
    private Workspace mWorkspace;

    private final ILauncherService mLauncherService = LauncherService.getLauncherService();

    @LauncherMethod
    public void onCreate(Bundle savedState) {
        initLauncherService();

        mModel = createLauncherModel();
        mWorkspace = createWorkspace();
    }

    @LauncherMethod
    public void onDestroy() {
        destroyLauncherService();
    }

    protected Context getContext() {
        return (Context) getInstance();
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

    private void initLauncherService() {
        try {
            sLauncherCallback = new LauncherCallback();
            mLauncherService.registerLauncherCallbacks(sLauncherCallback);
            initConfigurations(mLauncherService);
        } catch (Exception e) {
            Logger.log("Error registering callbacks service", e);
        }
    }

    private void destroyLauncherService() {
        try {
            if (sLauncherCallback != null) {
                mLauncherService.unregisterLauncherCallbacks(sLauncherCallback);
            }
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
        AppFilter.updateGlobalFilteredComponents(Set.copyOf(service.getPrivacyItems()));
    }

    public void onIdpChanged(boolean modelPropertiesChanged) {
        XposedHelpers.callMethod(getInstance(), "onIdpChanged", modelPropertiesChanged);
    }

    private class LauncherCallback extends ILauncherCallbacks.Stub {
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
            Executors.postUiThread(() -> {
                BubbleTextView.sDotParamsColor = color;
                onLauncherReload();
            });
        }

        @Override
        public void onDotParamsColorRestored() {
            Executors.postUiThread(() -> {
                BubbleTextView.sDotParamsColor = null;
                onLauncherReload();
            });
        }

        @Override
        public void onIconPackProviderChanged(String providerPkg) throws RemoteException {
            try {
                LauncherIconProvider.sIconProvider = providerPkg;
                LauncherIconProvider.sIconCaches.clear();
                getModel().getApp().refreshAndReloadLauncher();
            } catch (Exception e) { }
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
            onLauncherReload();
        }

        @Override
        public void onSpringLoadedBgEnable(boolean enable) throws RemoteException {
            CellLayout.sHideSpringLoadedBg = !enable;
        }

        @Override
        public void onQsbStateChanged(boolean enable) {
            onLauncherReload();
        }

        @Override
        public void onOpenedFolderCenterChanged(boolean center) {
            Folder.sCenterOpenedFolder = center;
        }

        @Override
        public void onPrivacyItemChange(ComponentName cn, boolean isPrivacy) throws RemoteException {
            Set<ComponentName> privacyItems = Set.copyOf(mLauncherService.getPrivacyItems());
            Executors.postUiThread(() -> {
                AppFilter.updateGlobalFilteredComponents(privacyItems);
                getModel().onPackageChanged(cn.getPackageName(), UserHandle.getUserHandleForUid(0));
            });
        }

        @Override
        public void onSetUseCustomSpringLoadedEffect(boolean use) {
            Executors.postUiThread(() -> {
                getModel().getApp().getIdp().onConfigChanged(getInstance());
                onIdpChanged(true);
            });
        }

        @Override
        public void onIconScaleChanged(float scale) {
            Executors.postUiThread(() -> {
                getModel().getApp().getIdp().onConfigChanged(getInstance());
                onIdpChanged(true);
            });
        }

        @Override
        public void onIconTextScaleChanged(float scale) {
            Executors.postUiThread(() -> {
                getModel().getApp().getIdp().onConfigChanged(getInstance());
                onIdpChanged(true);
            });
        }

        @Override
        public void onIconDrawablePaddingScaleChanged(float scale) {
            Executors.postUiThread(() -> {
                getModel().getApp().getIdp().onConfigChanged(getInstance());
                onIdpChanged(true);
            });
        }

        @Override
        public void onAllAppsIconVisibilityChanged(boolean visible) {
            Executors.postUiThread(() -> {
                getModel().getApp().getIdp().onConfigChanged(getInstance());
                onIdpChanged(true);
            });
        }
    }
}
