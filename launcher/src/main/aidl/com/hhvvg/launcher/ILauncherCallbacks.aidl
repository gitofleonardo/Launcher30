// ILauncherCallbacks.aidl
package com.hhvvg.launcher;

interface ILauncherCallbacks {
    void onIconClickEffectEnable(boolean enabled);
    void onComponentLabelUpdated(in ComponentName cn, in UserHandle user, in CharSequence label);
    void onDotParamsColorChanged(int color);
    void onDotParamsColorRestored();
    void onIconPackProviderChanged(in String providerPkg);
    void onLauncherReload();
    void onIconTextVisibilityChanged(boolean visible);
    void onDrawNotificationCountChanged(boolean enable);
    void onSpringLoadedBgEnable(boolean enable);
}
