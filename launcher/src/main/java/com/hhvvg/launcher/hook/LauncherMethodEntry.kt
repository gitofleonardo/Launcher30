package com.hhvvg.launcher.hook

import com.hhvvg.launcher.component.Component
import com.hhvvg.launcher.component.MethodInjection
import java.lang.reflect.Method
import kotlin.reflect.KClass

data class LauncherMethodEntry<T : Component>(
    val component: KClass<T>,
    val launcherClass: KClass<*>,
    val isConstructor: Boolean,
    val method: Method,
    val launcherMethodName: String,
    val methodInjection: Set<MethodInjection>,
    val methodArgTypes: List<KClass<*>>
)