package com.hhvvg.launcher.icon;

import android.animation.ObjectAnimator;
import android.os.RemoteException;
import android.util.FloatProperty;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.utils.ExtensionsKt;

@LauncherComponent(className = "com.android.launcher3.icons.FastBitmapDrawable")
public class FastBitmapDrawable extends Component {
    private static final Interpolator INTERPOLATOR = new PathInterpolator(0, .57f, .33f, 1f);
    private static final float ICON_SCALE = 0.8F;
    private static final long DURATION = 400;

    public static volatile boolean sClickEffectEnable = true;

    @LauncherMethod
    public void onStateChange(int[] state) throws RemoteException {
        if (!sClickEffectEnable) {
            return;
        }
        if (isPressed()) {
            if (getScaleAnimator() != null) {
                getScaleAnimator().cancel();
            }
            ObjectAnimator animator = ObjectAnimator.ofFloat(getInstance(), getScaleProperty(), ICON_SCALE);
            animator.setDuration(DURATION);
            animator.setInterpolator(INTERPOLATOR);
            animator.start();
            setScaleAnimator(animator);
        }
    }

    private boolean isPressed() {
        return ExtensionsKt.getBooleanField(getInstanceNonNull(), "mIsPressed");
    }

    private ObjectAnimator getScaleAnimator() {
        return ExtensionsKt.getObjectField(getInstanceNonNull(), "mScaleAnimation");
    }

    private void setScaleAnimator(ObjectAnimator animator) {
        ExtensionsKt.setObjectField(getInstanceNonNull(), "mScaleAnimation", animator);
    }

    private FloatProperty<Object> getScaleProperty() {
        return ExtensionsKt.getStaticField(getInstanceNonNull().getClass(), "SCALE");
    }
}
