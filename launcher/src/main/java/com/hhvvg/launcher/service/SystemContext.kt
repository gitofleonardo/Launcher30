package com.hhvvg.launcher.service

import android.content.Context
import android.content.SharedPreferences
import de.robv.android.xposed.XposedHelpers
import java.io.File

private const val DATA_DIR = "/data/system/launcher3customizer/shared_prefs"

fun Context.getSystemSharedPreferences(name: String, mode: Int): SharedPreferences {
    val dataDir = File(DATA_DIR)
    if (!dataDir.exists() && !dataDir.mkdirs()) {
        throw RuntimeException("Failed to create data dir")
    }
    val f = File("$DATA_DIR/$name")
    return XposedHelpers.callMethod(this, "getSharedPreferences", f, mode) as SharedPreferences
}
