package com.hhvvg.launcher.icon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherArgs;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.utils.Logger;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class DotRenderer extends LauncherComponent {
    private static final String CLASS = "com.android.launcher3.icons.DotRenderer";

    @LauncherMethod(inject = Inject.Before)
    public void override_draw(XC_MethodHook.MethodHookParam hookParam, Canvas canvas,
                              @LauncherArgs(className = "com.android.launcher3.icons.DotRenderer$DrawParams") DrawParams params) {
        Logger.log("1");
        if (params.getInstance() == null) {
            return;
        }
        Logger.log("2");
        canvas.save();

        Rect iconBounds = params.getIconBounds();
        float[] dotPosition = params.isLeftAlign() ? getLeftDotPosition() : getRightDotPosition();
        float dotCenterX = iconBounds.left + iconBounds.width() * dotPosition[0];
        float dotCenterY = iconBounds.top + iconBounds.height() * dotPosition[1];

        // Ensure dot fits entirely in canvas clip bounds.
        Rect canvasBounds = canvas.getClipBounds();
        float offsetX = params.isLeftAlign()
                ? Math.max(0, canvasBounds.left - (dotCenterX + getBitmapOffset()))
                : Math.min(0, canvasBounds.right - (dotCenterX - getBitmapOffset()));
        float offsetY = Math.max(0, canvasBounds.top - (dotCenterY + getBitmapOffset()));

        // We draw the dot relative to its center.
        canvas.translate(dotCenterX + offsetX, dotCenterY + offsetY);
        canvas.scale(params.getScale(), params.getScale());

        getCirclePaint().setColor(Color.BLACK);
        canvas.drawBitmap(getBackgroundWithShadow(), getBitmapOffset(), getBitmapOffset(), getCirclePaint());
        getCirclePaint().setColor(params.getDotColor());
        canvas.drawCircle(0, 0, getCircleRadius(), getCirclePaint());
        getCirclePaint().setColor(Color.WHITE);
        canvas.drawText("00", 0, 0, getCirclePaint());
        canvas.restore();

        hookParam.args[1] = null;
        Logger.log("3");
    }

    private float getCircleRadius() {
        return XposedHelpers.getFloatField(getInstance(), "mCircleRadius");
    }

    private Bitmap getBackgroundWithShadow() {
        return (Bitmap) XposedHelpers.getObjectField(getInstance(), "mBackgroundWithShadow");
    }

    private float[] getLeftDotPosition() {
        return (float[]) XposedHelpers.getObjectField(getInstance(), "mLeftDotPosition");
    }

    private float[] getRightDotPosition() {
        return (float[]) XposedHelpers.getObjectField(getInstance(), "mRightDotPosition");
    }

    private float getBitmapOffset() {
        return XposedHelpers.getFloatField(getInstance(), "mBitmapOffset");
    }

    private Paint getCirclePaint() {
        return (Paint) XposedHelpers.getObjectField(getInstance(), "mCirclePaint");
    }

    @NonNull
    @Override
    public String getClassName() {
        return CLASS;
    }
}
