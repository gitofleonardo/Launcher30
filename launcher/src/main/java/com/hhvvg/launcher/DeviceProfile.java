package com.hhvvg.launcher;

import android.content.Context;
import android.content.res.Resources;
import android.os.RemoteException;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.service.LauncherService;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.DeviceProfile")
public class DeviceProfile extends Component {
    private ILauncherService mService;

    private ILauncherService getService() {
        if (mService == null) {
            mService = LauncherService.getLauncherService();
        }
        return mService;
    }

    @LauncherMethod
    public void $updateIconSize(XC_MethodHook.MethodHookParam param, float scale, Resources resources) throws RemoteException {
        if (!getService().isIconTextVisible()) {
            setIconTextSize(0);
        }

        if (getService().isUseCustomSpringLoadedEffect()) {
            XposedHelpers.setIntField(getInstance(), "dropTargetBarTopMarginPx", 0);
            XposedHelpers.setIntField(getInstance(), "dropTargetBarBottomMarginPx", 0);
        }
    }

    @LauncherMethod
    public void $updateFolderCellSize(XC_MethodHook.MethodHookParam param, float scale, Resources resources) throws RemoteException {
        if (!getService().isIconTextVisible()) {
            setFolderChildTextSize(0);
        }
    }

    @LauncherMethod
    public void $getWorkspaceSpringLoadScale(XC_MethodHook.MethodHookParam param, Context context) throws RemoteException {
        if (getService().isUseCustomSpringLoadedEffect()) {
            float result = (float) param.getResult();
            param.setResult(1f);
        }
    }

    private void setFolderChildTextSize(int sizePx) {
        XposedHelpers.setIntField(getInstance(), "folderChildTextSizePx", sizePx);
    }

    private void setIconTextSize(int sizePx) {
        XposedHelpers.setIntField(getInstance(), "iconTextSizePx", sizePx);
    }
}
