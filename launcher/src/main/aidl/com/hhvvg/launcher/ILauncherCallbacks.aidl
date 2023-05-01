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
    void onQsbStateChanged(boolean enable);
    void onOpenedFolderCenterChanged(boolean center);
    void onPrivacyItemChange(in ComponentName cn, boolean isPrivacy);
    void onSetUseCustomSpringLoadedEffect(boolean use);
    void onIconScaleChanged(float scale);
    void onIconTextScaleChanged(float scale);
    void onIconDrawablePaddingScaleChanged(float scale);
    void onAllAppsIconVisibilityChanged(boolean visible);
}
