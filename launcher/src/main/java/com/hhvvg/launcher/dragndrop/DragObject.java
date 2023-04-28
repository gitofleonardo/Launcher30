package com.hhvvg.launcher.dragndrop;

import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherComponent;

import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.DropTarget$DragObject")
public class DragObject extends Component {

    public float getY() {
        return XposedHelpers.getFloatField(getInstance(), "y");
    }

    public float getX() {
        return XposedHelpers.getFloatField(getInstance(), "x");
    }
}
