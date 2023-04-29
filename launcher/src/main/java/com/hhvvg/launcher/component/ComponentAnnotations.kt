package com.hhvvg.launcher.component

@Target(allowedTargets = [AnnotationTarget.FUNCTION])
annotation class LauncherMethod(
    val injections: Array<MethodInjection> = [MethodInjection.After]
)

@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class LauncherComponent(
    val className: String
)

enum class MethodInjection {
    Before,
    After
}
