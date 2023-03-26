package com.hhvvg.launcher3customizer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.skydoves.colorpickerview.ColorPickerView;

public class MyColorPickerView extends ColorPickerView {
    public MyColorPickerView(Context context) {
        super(context);
    }

    public MyColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyColorPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                requestDisallowInterceptTouchEvent(false);
                break;
            }
        }
        return super.onTouchEvent(event);
    }
}
