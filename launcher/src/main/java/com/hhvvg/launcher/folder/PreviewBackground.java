package com.hhvvg.launcher.folder;

import androidx.annotation.NonNull;

import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.icon.BubbleTextView;

import de.robv.android.xposed.XC_MethodHook;

public class PreviewBackground extends LauncherComponent {
    @NonNull
    @Override
    public String getClassName() {
        return "com.android.launcher3.folder.PreviewBackground";
    }

    @LauncherMethod(inject = Inject.After)
    public void override_getDotColor(XC_MethodHook.MethodHookParam param) {
        if (BubbleTextView.sDotParamsColor != null) {
            param.setResult(BubbleTextView.sDotParamsColor);
        }
    }
}
