// ILauncherCallbacks.aidl
package com.hhvvg.launcher;

interface ILauncherCallbacks {
    void onIconClickEffectEnable(boolean enabled);
    void onComponentLabelUpdated(in ComponentName cn, in UserHandle user, in CharSequence label);
}
