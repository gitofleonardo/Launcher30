package com.hhvvg.launcher.icon;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.icons.IconNormalizer")
public class IconNormalizer extends Component {

    public float getScale(Drawable d, RectF outBounds, Path path, boolean[] outMaskShape) {
        return (float) XposedHelpers.callMethod(getInstance(), "getScale", d, outBounds,
                path, outMaskShape);
    }
}
