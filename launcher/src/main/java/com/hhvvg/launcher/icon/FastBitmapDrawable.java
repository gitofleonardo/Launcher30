package com.hhvvg.launcher.icon;

import androidx.annotation.NonNull;

import android.animation.ObjectAnimator;
import android.os.RemoteException;
import android.util.FloatProperty;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.utils.ExtensionsKt;
import com.hhvvg.launcher.utils.Logger;

import de.robv.android.xposed.XC_MethodHook;

public class FastBitmapDrawable extends LauncherComponent {
    private static final Interpolator ACCEL = new AccelerateDecelerateInterpolator();
    private static final float ICON_SCALE = 0.8F;
    private static final long DURATION = 100;

    public static boolean CLICK_EFFECT_ENABLE = true;


    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.icons.FastBitmapDrawable";
    }

    @LauncherMethod(inject = Inject.After)
    public void override_onStateChange(XC_MethodHook.MethodHookParam param, int[] state) throws RemoteException {
        if (!CLICK_EFFECT_ENABLE) {
            return;
        }
        if (isPressed()) {
            if (getScaleAnimator() != null) {
                getScaleAnimator().cancel();
            }
            ObjectAnimator animator = ObjectAnimator.ofFloat(getInstance(), getScaleProperty(), ICON_SCALE);
            animator.setDuration(DURATION);
            animator.setInterpolator(ACCEL);
            animator.start();
            setScaleAnimator(animator);
        }
    }

    private boolean isPressed() {
        return ExtensionsKt.getBooleanField(getInstance(), "mIsPressed");
    }

    private ObjectAnimator getScaleAnimator() {
        return ExtensionsKt.getObjectField(getInstance(), "mScaleAnimation");
    }

    private void setScaleAnimator(ObjectAnimator animator) {
        ExtensionsKt.setObjectField(getInstance(), "mScaleAnimation", animator);
    }

    private FloatProperty<Object> getScaleProperty() {
        return ExtensionsKt.getStaticField(getInstance().getClass(), "SCALE");
    }
}
