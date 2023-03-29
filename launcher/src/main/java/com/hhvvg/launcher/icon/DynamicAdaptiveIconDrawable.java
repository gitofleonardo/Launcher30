package com.hhvvg.launcher.icon;

import android.graphics.Path;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;

public class DynamicAdaptiveIconDrawable extends AdaptiveIconDrawable {
    private final Path mMask;
    public DynamicAdaptiveIconDrawable(Drawable backgroundDrawable,
                                       Drawable foregroundDrawable, Path mask) {
        super(backgroundDrawable, foregroundDrawable);
        mMask = mask;
    }

    @Override
    public Path getIconMask() {
        return mMask;
    }
}