package com.hhvvg.launcher.icon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.PathParser;

import com.hhvvg.launcher.ILauncherService;
import com.hhvvg.launcher.Init;
import com.hhvvg.launcher.R;
import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.service.LauncherService;
import com.hhvvg.launcher.utils.Logger;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class LauncherIcons extends LauncherComponent {
    private static final String CLASS_NAME = "com.android.launcher3.icons.LauncherIcons";
    private ILauncherService mLauncherService;
    private IconNormalizer mNormalizer;

    private ILauncherService getService() {
        if (mLauncherService == null) {
            mLauncherService = LauncherService.getLauncherService();
        }
        return mLauncherService;
    }

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
                                                        RectF outIconBounds, float[] outScale) throws RemoteException {
        if (icon == null) {
            return;
        }
        float scale = 1f;
        param.args[1] = shrinkNonAdaptiveIcons = getService().isAdaptiveIconEnable();

        if (shrinkNonAdaptiveIcons && !(icon instanceof AdaptiveIconDrawable)) {
            Drawable wrapper = createDrawableWrapper().mutate();
            setWrapperIcon(wrapper);
            AdaptiveIconDrawable dr = (AdaptiveIconDrawable) wrapper;
            dr.setBounds(0, 0, 1, 1);
            boolean[] outShape = new boolean[1];
            scale = getNormalizer().getScale(icon, outIconBounds, dr.getIconMask(), outShape);
            Logger.log("scale: " + scale + ", outshape: " + outShape[0]);
            if (!outShape[0]) {
                FixedScaleDrawable fsd = (FixedScaleDrawable) dr.getForeground();
                fsd.setDrawable(icon);
                fsd.setScale(scale);
                icon = dr;
                scale = getNormalizer().getScale(icon, outIconBounds, null, null);
                ((ShapeDrawable) dr.getBackground()).setTint(Color.WHITE);
            }
        } else {
            scale = getNormalizer().getScale(icon, outIconBounds, null, null);
        }

        outScale[0] = scale;
        param.setResult(icon);
    }

    private AdaptiveIconDrawable createDrawableWrapper() {
        ShapeDrawable sd = new ShapeDrawable();
        RoundRectShape shape = new RoundRectShape(new float[] {
                10, 10, 10, 10,
                10, 10, 10, 10
        }, null, null);
        sd.setShape(shape);
        sd.getPaint().setColor(Color.WHITE);
        sd.getPaint().setAntiAlias(true);
        sd.getPaint().setStyle(Paint.Style.FILL);

        FixedScaleDrawable foreground = new FixedScaleDrawable();

        return new AdaptiveIconDrawable(sd, foreground);
    }

    private IconNormalizer getNormalizer() {
        if (mNormalizer == null) {
            mNormalizer = new IconNormalizer();
            Object normalizer = XposedHelpers.callMethod(getInstance(), "getNormalizer");
            mNormalizer.setInstance(normalizer);
        }
        return mNormalizer;
    }

    private Context getContext() {
        return (Context) XposedHelpers.getObjectField(getInstance(), "mContext");
    }

    private Drawable getWrapperIcon() {
        return (Drawable) XposedHelpers.getObjectField(getInstance(), "mWrapperIcon");
    }

    private void setWrapperIcon(Drawable drawable) {
        XposedHelpers.setObjectField(getInstance(), "mWrapperIcon", drawable);
    }

    @NonNull
    @Override
    public String getClassName() {
        return CLASS_NAME;
    }
}
