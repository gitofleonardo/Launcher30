package com.hhvvg.launcher.utils;

import com.hhvvg.launcher.Init;

import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.robv.android.xposed.XposedHelpers;

public class Executors {
    private static final String CLASS = "com.android.launcher3.util.Executors";

    private static final Executor sWorkerExecutor = new ThreadPoolExecutor(
            1,
            10,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>()
    );

    public static void postUiThread(Runnable task) {
        Class<?> clazz = XposedHelpers.findClass(CLASS, Init.classLoader);
        Object executor = XposedHelpers.getStaticObjectField(clazz, "MAIN_EXECUTOR");
        XposedHelpers.callMethod(executor, "execute", task);
    }

    public static void postModelThread(Runnable task) {
        Class<?> clazz = XposedHelpers.findClass(CLASS, Init.classLoader);
        Object executor = XposedHelpers.getStaticObjectField(clazz, "MODEL_EXECUTOR");
        XposedHelpers.callMethod(executor, "execute", task);
    }

    public static void postWorkerThread(Runnable task) {
        sWorkerExecutor.execute(task);
    }
}
