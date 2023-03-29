package com.hhvvg.launcher.icon;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hhvvg.launcher.Init;
import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.utils.Logger;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class LauncherIcons extends LauncherComponent {
    private static final String CLASS_NAME = "com.android.launcher3.icons.LauncherIcons";

    public static LauncherIcons obtain(Context context) {
        try {
            Class<?> clz = XposedHelpers.findClass(CLASS_NAME, Init.classLoader);
            Object li = XposedHelpers.callStaticMethod(clz, "obtain", context);
            LauncherIcons thisLi = new LauncherIcons();
            thisLi.setInstance(li);
            return thisLi;
        } catch (Exception e) {
            Logger.log("Error", e);
            return null;
        }
    }

    @Nullable
    public BitmapInfo createBadgedIconBitmap(Drawable drawable) {
        try {
            // TODO Add A Badge
            Object bitmapInfo = XposedHelpers.callMethod(getInstance(), "createBadgedIconBitmap", drawable, null);
            if (bitmapInfo == null) {
                return null;
            }
            BitmapInfo thisBitmapInfo = new BitmapInfo();
            thisBitmapInfo.setInstance(bitmapInfo);
            return thisBitmapInfo;
        } catch (Exception e) {
            return null;
        }
    }

    @LauncherMethod(inject = Inject.Before)
    public void override_normalizeAndWrapToAdaptiveIcon(XC_MethodHook.MethodHookParam param,
                                                        Drawable icon, boolean shrinkNonAdaptiveIcons,
                                                        RectF outIconBounds, float[] outScale) {
        param.args[1] = false;
    }

    @NonNull
    @Override
    public String getClassName() {
        return CLASS_NAME;
    }
}
