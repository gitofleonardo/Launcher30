package com.hhvvg.launcher.folder;

import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.icon.BubbleTextView;

import de.robv.android.xposed.XC_MethodHook;

@LauncherComponent(className = "com.android.launcher3.folder.PreviewBackground")
public class PreviewBackground extends Component {

    @LauncherMethod
    public void getDotColor(XC_MethodHook.MethodHookParam param) {
        if (BubbleTextView.sDotParamsColor != null) {
            param.setResult(BubbleTextView.sDotParamsColor);
        }
    }
}
