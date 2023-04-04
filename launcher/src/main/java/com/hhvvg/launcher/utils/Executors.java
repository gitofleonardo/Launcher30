package com.hhvvg.launcher.utils;

import com.hhvvg.launcher.Init;

import de.robv.android.xposed.XposedHelpers;

public class Executors {
    private static final String CLASS = "com.android.launcher3.util.Executors";

    public static void postUiThread(Runnable task) {
        Class<?> clazz = XposedHelpers.findClass(CLASS, Init.classLoader);
        Object executor = XposedHelpers.getStaticObjectField(clazz, "MAIN_EXECUTOR");
        XposedHelpers.callMethod(executor, "execute", task);
        Logger.log(executor.toString());
    }

    public static void postModelThread(Runnable task) {
        Class<?> clazz = XposedHelpers.findClass(CLASS, Init.classLoader);
        Object executor = XposedHelpers.getStaticObjectField(clazz, "MODEL_EXECUTOR");
        XposedHelpers.callMethod(executor, "execute", task);
    }
}
