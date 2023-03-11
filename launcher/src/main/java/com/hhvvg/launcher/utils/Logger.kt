package com.hhvvg.launcher.utils

import de.robv.android.xposed.XposedBridge

/**
 * @author hhvvg
 */
class Logger {
    companion object {
        @JvmStatic
        fun log(log: String) {
            XposedBridge.log(log)
        }

        @JvmStatic
        fun log(log: String, t: Throwable) {
            log(log)
            XposedBridge.log(t)
        }
    }
}