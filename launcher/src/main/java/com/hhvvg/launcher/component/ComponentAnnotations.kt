package com.hhvvg.launcher.component

@Target(allowedTargets = [AnnotationTarget.FUNCTION])
annotation class LauncherMethod

@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class LauncherComponent(
    val className: String
)

enum class MethodInjection {
    Before,
    After
}
