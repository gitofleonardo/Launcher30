package com.hhvvg.launcher;

import android.content.Context;
import android.content.res.Resources;
import android.os.RemoteException;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.component.MethodInjection;
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
    public void updateIconSize(float scale, Resources resources) throws RemoteException {
        if (!getService().isIconTextVisible()) {
            setIconTextSize(0);
        }

        if (getService().isUseCustomSpringLoadedEffect()) {
            XposedHelpers.setIntField(getInstance(), "dropTargetBarTopMarginPx", 0);
            XposedHelpers.setIntField(getInstance(), "dropTargetBarBottomMarginPx", 0);
        }

        float iconScale = getService().getIconScale();
        if (Float.compare(iconScale, 1.0f) != 0) {
            setIconScale(iconScale);
        }

        float iconTextScale = getService().getIconTextScale();
        if (Float.compare(iconTextScale, 1.0f) != 0) {
            setIconTextScale(iconTextScale);
        }

        float iconDrawablePaddingScale = getService().getIconDrawablePaddingScale();
        if (Float.compare(iconDrawablePaddingScale, 1.0f) != 0) {
            setIconDrawablePaddingScale(iconDrawablePaddingScale);
        }
    }

    @LauncherMethod
    public void updateAllAppsIconSize(float scale, Resources res) throws RemoteException {
        if (!getService().isAllAppsIconTextVisible()) {
            setAllAppsIconTextSize(0);
        }

        float iconTextScale = getService().getIconTextScale();
        if (Float.compare(iconTextScale, 1.0f) != 0) {
            float allAppsTextSize = getAllAppsIconTextSize();
            float finalAllAppsTextSize = allAppsTextSize * iconTextScale;
            setAllAppsIconTextSize(finalAllAppsTextSize);
        }

        float iconScale = getService().getIconScale();
        if (Float.compare(iconScale, 1.0f) != 0) {
            int allAppsIconSize = getAllAppsIconSize();
            int finalAllAppsIconSize = (int) (allAppsIconSize * iconScale);
            setAllAppsIconSize(finalAllAppsIconSize);

            int allAppsCellHeight = getAllAppsCellHeight();
            int finalCellHeight = (int) (allAppsCellHeight * iconScale);
            setAllAppsCellHeight(finalCellHeight);
        }

        float iconDrawablePaddingScale = getService().getIconDrawablePaddingScale();
        if (Float.compare(iconDrawablePaddingScale, 1.0f) != 0) {
            int allAppsDrawablePadding = getAllAppsIconDrawablePadding();
            int finalPadding = (int) (allAppsDrawablePadding * iconDrawablePaddingScale);
            setAllAppsIconDrawablePadding(finalPadding);
        }
    }

    @LauncherMethod
    public void updateFolderCellSize(float scale, Resources resources) throws RemoteException {
        if (!getService().isIconTextVisible()) {
            setFolderChildTextSize(0);
        }

        float iconTextScale = getService().getIconTextScale();
        if (Float.compare(iconTextScale, 1.0f) != 0) {
            int folderChildTextSize = getFolderChildTextSize();
            int finalFolderChildTextSize = (int) (folderChildTextSize * iconTextScale);
            setFolderChildTextSize(finalFolderChildTextSize);
        }

        float iconScale = getService().getIconScale();
        if (Float.compare(iconScale, 1.0f) != 0) {
            int folderChildIconSize = getFolderChildIconSize();
            int finalFolderChildIconSize = (int) (folderChildIconSize * iconScale);
            setFolderChildIconSize(finalFolderChildIconSize);
        }

        float iconDrawablePaddingScale = getService().getIconDrawablePaddingScale();
        if (Float.compare(iconDrawablePaddingScale, 1.0f) != 0) {
            int folderDrawablePadding = getFolderChildIconDrawablePadding();
            int finalPadding = (int) (folderDrawablePadding * iconDrawablePaddingScale);
            setFolderChildIconDrawablePadding(finalPadding);
        }
    }

    @LauncherMethod
    public void getWorkspaceSpringLoadScale(XC_MethodHook.MethodHookParam param, Context context) throws RemoteException {
        if (getService().isUseCustomSpringLoadedEffect()) {
            param.setResult(1f);
        }
    }

    private int getFolderChildIconSize() {
        return XposedHelpers.getIntField(getInstanceNonNull(), "folderChildIconSizePx");
    }

    private void setFolderChildIconSize(int size) {
        XposedHelpers.setIntField(getInstanceNonNull(), "folderChildIconSizePx", size);
    }

    private int getFolderChildTextSize() {
        return XposedHelpers.getIntField(getInstanceNonNull(), "folderChildTextSizePx");
    }

    private void setFolderChildIconDrawablePadding(int padding) {
        XposedHelpers.setIntField(getInstanceNonNull(), "folderChildDrawablePaddingPx", padding);
    }

    private int getFolderChildIconDrawablePadding() {
        return XposedHelpers.getIntField(getInstanceNonNull(), "folderChildDrawablePaddingPx");
    }

    private void setFolderChildTextSize(int sizePx) {
        XposedHelpers.setIntField(getInstance(), "folderChildTextSizePx", sizePx);
    }

    private void setIconTextSize(int sizePx) {
        XposedHelpers.setIntField(getInstance(), "iconTextSizePx", sizePx);
    }

    private void setIconScale(float scale) {
        XposedHelpers.setFloatField(getInstanceNonNull(), "iconScale", scale);
        scaleIconSize(scale);
    }

    private void setAllAppsIconSize(int size) {
        XposedHelpers.setIntField(getInstanceNonNull(), "allAppsIconSizePx", size);
    }

    private int getAllAppsIconSize() {
        return XposedHelpers.getIntField(getInstanceNonNull(), "allAppsIconSizePx");
    }

    private void setAllAppsIconTextSize(float size) {
        XposedHelpers.setFloatField(getInstanceNonNull(), "allAppsIconTextSizePx", size);
    }

    private float getAllAppsIconTextSize() {
        return XposedHelpers.getFloatField(getInstanceNonNull(), "allAppsIconTextSizePx");
    }

    private void setAllAppsIconDrawablePadding(int padding) {
        XposedHelpers.setIntField(getInstanceNonNull(), "allAppsIconDrawablePaddingPx", padding);
    }

    private int getAllAppsIconDrawablePadding() {
        return XposedHelpers.getIntField(getInstanceNonNull(), "allAppsIconDrawablePaddingPx");
    }

    private void setIconTextScale(float scale) {
        int iconTextSize = getIconTextSize();
        int finalIconTextSize = (int) (iconTextSize * scale);
        XposedHelpers.setIntField(getInstanceNonNull(), "iconTextSizePx", finalIconTextSize);
    }

    private void scaleIconSize(float scale) {
        int iconSize = getIconSize();
        int finalIconSize = (int) (iconSize * scale);
        XposedHelpers.setIntField(getInstanceNonNull(), "iconSizePx", finalIconSize);
        XposedHelpers.setIntField(getInstanceNonNull(), "folderIconSizePx", finalIconSize);
    }

    private int getIconSize() {
        return XposedHelpers.getIntField(getInstanceNonNull(), "iconSizePx");
    }

    private int getIconTextSize() {
        return XposedHelpers.getIntField(getInstanceNonNull(), "iconTextSizePx");
    }

    private void setIconDrawablePaddingScale(float scale) {
        int padding = getIconDrawablePaddingPx();
        final int finalPaddingPx = (int) (scale * padding);
        setIconDrawablePaddingPx(finalPaddingPx);
    }

    private int getIconDrawablePaddingPx() {
        return XposedHelpers.getIntField(getInstanceNonNull(), "iconDrawablePaddingPx");
    }

    private void setIconDrawablePaddingPx(int paddingPx) {
        XposedHelpers.setIntField(getInstanceNonNull(), "iconDrawablePaddingPx", paddingPx);
    }

    private void setAllAppsCellHeight(int cellHeight) {
        XposedHelpers.setIntField(getInstanceNonNull(), "allAppsCellHeightPx", cellHeight);
    }

    private int getAllAppsCellHeight() {
        return XposedHelpers.getIntField(getInstanceNonNull(), "allAppsCellHeightPx");
    }
}
