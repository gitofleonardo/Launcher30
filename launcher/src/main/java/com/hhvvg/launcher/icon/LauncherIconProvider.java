package com.hhvvg.launcher.icon;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.LauncherActivityInfo;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.model.IconModel;

import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class LauncherIconProvider extends LauncherComponent {
    public static String sIconProvider = null;
    public static final Map<ComponentName, IconModel.LauncherIconResource> sIconCaches = new HashMap<>();
    private final IconModel mModel = new IconModel();

    @LauncherMethod(inject = Inject.After)
    public void override_getIcon(XC_MethodHook.MethodHookParam param, LauncherActivityInfo info, int dpi) {
        if (sIconCaches.isEmpty()) {
            mModel.loadAllIcons(sIconProvider, getContext(), sIconCaches);
        }
        ComponentName cn = info.getComponentName();
        IconModel.LauncherIconResource icon = sIconCaches.get(cn);
        if (icon == null) {
            return;
        }
        Drawable d = mModel.getDrawable(icon);
        if (d == null) {
            return;
        }
        param.setResult(d);
    }

    private Context getContext() {
        return (Context) XposedHelpers.getObjectField(getInstance(), "mContext");
    }

    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.icons.LauncherIconProvider";
    }
}
