package com.hhvvg.launcher.icon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.component.MethodInjection;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.icons.DotRenderer")
public class DotRenderer extends Component {
    private static final String EXCEED_TEXT = "···";
    private static final float NOTIFICATION_COUNT_TEXT_SIZE_RATIO = 0.8f;
    private static final float ROUND_RECT_RATIO = 1.3f;
    private static final RectF sTmpRect = new RectF();

    public static boolean sDrawNotificationCount = true;

    @LauncherMethod(injections = MethodInjection.Before)
    public void draw(XC_MethodHook.MethodHookParam hookParam, Canvas canvas, DrawParams params) {
        if (params.getInstance() == null) {
            return;
        }
        if (!sDrawNotificationCount) {
            return;
        }
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

        int notificationCount = params.getNotificationCount();
        boolean drawRoundRect = notificationCount >= 10;
        String countText;
        if (notificationCount >= 100) {
            countText = EXCEED_TEXT;
        } else {
            countText = String.valueOf(notificationCount);
        }
        float radius = getCircleRadius();
        Paint circlePaint = getCirclePaint();

        if (drawRoundRect) {
            float rectExtraSideWidth = radius * ROUND_RECT_RATIO - radius;
            sTmpRect.set(-radius - rectExtraSideWidth, -radius, radius + rectExtraSideWidth, radius);
            canvas.drawRoundRect(sTmpRect, radius, radius, circlePaint);
        } else {
            canvas.drawCircle(0, 0, getCircleRadius(), getCirclePaint());
        }

        getCirclePaint().setColor(Color.WHITE);
        if (notificationCount > 0) {
            Paint textPaint = getCirclePaint();
            textPaint.setTextSize(getTextSize());
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);

            Paint.FontMetrics fm = textPaint.getFontMetrics();
            float fontHeight = fm.descent - fm.ascent;
            float textOffsetY = fontHeight / 2 - fm.bottom;
            float textOffsetX = textPaint.measureText(countText) / 2;

            canvas.drawText(countText, -textOffsetX, textOffsetY, textPaint);
        }

        canvas.restore();

        hookParam.args[1] = null;
    }

    private float getTextSize() {
        return getCircleRadius() * 2 * NOTIFICATION_COUNT_TEXT_SIZE_RATIO;
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
}
