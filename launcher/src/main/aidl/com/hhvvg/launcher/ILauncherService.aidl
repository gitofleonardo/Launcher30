// ILauncherService.aidl
package com.hhvvg.launcher;

import com.hhvvg.launcher.ILauncherCallbacks;

interface ILauncherService {
    void registerLauncherCallbacks(in ILauncherCallbacks callbacks);
    void unregisterLauncherCallbacks(in ILauncherCallbacks callbacks);

    void setComponentLabel(in ComponentName cn, in UserHandle user, CharSequence title);
    CharSequence getComponentLabel(in ComponentName cn, in UserHandle user);

    void setClickEffectEnable(boolean enabled);
    boolean isClickEffectEnable();

    void setDotParamsColor(int color);
    void restoreDotParamsColor();
    int getDotParamsColor();

    void resetAppFavorites(in ComponentName cn, in UserHandle user);

    void setIconPackProvider(in String providerPkg);
    String getIconPackProvider();

    void setAdaptiveIconEnable(boolean enable);
    boolean isAdaptiveIconEnable();

    void forceReloadLauncher();

    void setIconTextVisible(boolean visible);
    boolean isIconTextVisible();

    void setDrawNotificationCount(boolean enable);
    boolean isDrawNotificationCount();

    void setSpringLoadedBgEnable(boolean enable);
    boolean isSpringLoadedBgEnable();

    void setQsbEnable(boolean enable);
    boolean isQsbEnable();

    void setOpenedFolderCenter(boolean center);
    boolean isOpenedFolderCenter();

    void changePrivacyItem(in ComponentName cn, boolean isPrivacy);
    List<ComponentName> getPrivacyItems();
}
