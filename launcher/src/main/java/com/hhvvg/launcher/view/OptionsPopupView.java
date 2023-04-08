package com.hhvvg.launcher.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.hhvvg.launcher.Init;
import com.hhvvg.launcher.Launcher;
import com.hhvvg.launcher.R;
import com.hhvvg.launcher.component.Inject;
import com.hhvvg.launcher.component.LauncherArgs;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.LauncherMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class OptionsPopupView extends LauncherComponent {
    private static final String CLASS = "com.android.launcher3.views.OptionsPopupView";

    @LauncherMethod(inject = Inject.After)
    public static void override_getOptions(XC_MethodHook.MethodHookParam param,
                                    @LauncherArgs(className = Launcher.CLASS) Launcher launcher) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Drawable drawable = ResourcesCompat.getDrawable(Init.xModuleRes, R.drawable.ic_palette_24, null);
        String title = Init.xModuleRes.getString(R.string.title_customization);
        OptionItem item = OptionItem.buildOptionItem(
                title,
                drawable,
                OptionsPopupView::onOpenLauncher30
        );

        ArrayList result = (ArrayList) param.getResult();
        result.add(item.getInstance());
    }

    private static boolean onOpenLauncher30(View v) {
        Intent intent = new Intent("com.hhvvg.launcher30.home_activity");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        v.getContext().startActivity(intent);
        return true;
    }

    public static class OptionItem extends LauncherComponent {
        private static final String CLASS = "com.android.launcher3.views.OptionsPopupView$OptionItem";
        private static final String EVENT_ENUM_CLASS = "com.android.launcher3.logging.StatsLogManager$EventEnum";
        private static final String LAUNCHER_EVENT_ENUM_CLASS = "com.android.launcher3.logging.StatsLogManager$LauncherEvent";

        public static OptionItem buildOptionItem(CharSequence label, Drawable icon, View.OnLongClickListener listener) throws InvocationTargetException, IllegalAccessException, InstantiationException {
            Class<?> optionClazz = XposedHelpers.findClass(CLASS, Init.classLoader);
            Class<?> eventClazz = XposedHelpers.findClass(EVENT_ENUM_CLASS, Init.classLoader);
            Class<?> launcherEventClazz = XposedHelpers.findClass(LAUNCHER_EVENT_ENUM_CLASS, Init.classLoader);
            Constructor<?> constructor = XposedHelpers.findConstructorBestMatch(optionClazz,
                    CharSequence.class, Drawable.class, eventClazz, View.OnLongClickListener.class);
            Object instance = constructor.newInstance(label, icon, launcherEventClazz.getEnumConstants()[0], listener);

            OptionItem item = new OptionItem();
            item.setInstance(instance);
            return item;
        }

        @NonNull
        @Override
        public String getClassName() {
            return CLASS;
        }
    }

    @NonNull
    @Override
    public String getClassName() {
        return CLASS;
    }
}
