package com.hhvvg.launcher.component


@Target(allowedTargets = [AnnotationTarget.FUNCTION])
annotation class LauncherMethod(
    val inject: Inject,
)

@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
annotation class LauncherArgs (
    val className: String
)

enum class Inject {
    Before,
    After
}
