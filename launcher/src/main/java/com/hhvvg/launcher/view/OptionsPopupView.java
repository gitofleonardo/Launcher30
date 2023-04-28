package com.hhvvg.launcher.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.hhvvg.launcher.Init;
import com.hhvvg.launcher.Launcher;
import com.hhvvg.launcher.R;
import com.hhvvg.launcher.component.LauncherComponent;
import com.hhvvg.launcher.component.Component;
import com.hhvvg.launcher.component.LauncherMethod;
import com.hhvvg.launcher.component.ViewGroupComponent;
import com.hhvvg.launcher.hook.HookProviderKt;
import com.hhvvg.launcher.service.LauncherService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@LauncherComponent(className = "com.android.launcher3.views.OptionsPopupView")
public class OptionsPopupView extends ViewGroupComponent {

    @LauncherMethod
    public static void $getOptions(XC_MethodHook.MethodHookParam param,
                                    Launcher launcher) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Drawable drawable = ResourcesCompat.getDrawable(Init.xModuleRes, R.drawable.ic_palette_24, null);
        String title = Init.xModuleRes.getString(R.string.title_customization);
        OptionItem item = OptionItem.buildOptionItem(
                title,
                drawable,
                OptionsPopupView::onOpenLauncher30
        );

        OptionItem privacyItem = OptionItem.buildOptionItem(
                Init.xModuleRes.getString(R.string.title_privacy_apps),
                ResourcesCompat.getDrawable(Init.xModuleRes, R.drawable.ic_privacy_24, null),
                OptionsPopupView::onOpenPrivacyPage
        );

        ArrayList result = (ArrayList) param.getResult();
        result.add(item.getInstance());
        result.add(privacyItem.getInstance());
    }

    private static boolean onOpenPrivacyPage(View v) {
        try {
            Intent intent = new Intent();
            String targetAction = "com.hhvvg.launcher3customizer.privacy.ALL_APPS";
            if (LauncherService.getLauncherService().useBiometricPrivacyApps()) {
                intent.setAction("com.hhvvg.launcher.BIOMETRIC_PROXY");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("activity://com.hhvvg.launcher/biometricProxy?target=" + targetAction + "&allowDirectStart=true"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                intent.setAction(targetAction);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            v.getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static boolean onOpenLauncher30(View v) {
        Intent intent = new Intent("com.hhvvg.launcher30.home_activity");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        v.getContext().startActivity(intent);
        return true;
    }

    @LauncherComponent(className = "com.android.launcher3.views.OptionsPopupView$OptionItem")
    public static class OptionItem extends Component {
        private static final String EVENT_ENUM_CLASS = "com.android.launcher3.logging.StatsLogManager$EventEnum";
        private static final String LAUNCHER_EVENT_ENUM_CLASS = "com.android.launcher3.logging.StatsLogManager$LauncherEvent";

        public static OptionItem buildOptionItem(CharSequence label, Drawable icon, View.OnLongClickListener listener) throws InvocationTargetException, IllegalAccessException, InstantiationException {
            Class<?> optionClazz = HookProviderKt.getLauncherJavaClass(OptionItem.class);
            Class<?> eventClazz = XposedHelpers.findClass(EVENT_ENUM_CLASS, Init.classLoader);
            Class<?> launcherEventClazz = XposedHelpers.findClass(LAUNCHER_EVENT_ENUM_CLASS, Init.classLoader);
            Constructor<?> constructor = XposedHelpers.findConstructorBestMatch(optionClazz,
                    CharSequence.class, Drawable.class, eventClazz, View.OnLongClickListener.class);
            Object instance = constructor.newInstance(label, icon, launcherEventClazz.getEnumConstants()[0], listener);

            OptionItem item = new OptionItem();
            item.setInstance(instance);
            return item;
        }
    }
}
