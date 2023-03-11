package com.hhvvg.launcher.user

import android.content.ComponentName
import android.os.UserHandle

/**
 * @author hhvvg
 */
class ComponentKey(
    val componentName: ComponentName,
    val user: UserHandle
) {
    private val userHashCode = user.hashCode()

    override fun toString(): String {
        return "${componentName.flattenToString()}#${userHashCode}"
    }

    companion object {
        @JvmStatic
        fun fromString(str: String): ComponentKey? {
            val sep = str.indexOf('#')
            if (sep < 0 || sep + 1 >= str.length) {
                return null
            }
            val componentName =
                ComponentName.unflattenFromString(str.substring(0, sep)) ?: return null
            return try {
                ComponentKey(
                    componentName,
                    UserHandle.getUserHandleForUid(str.substring(sep + 1).toInt())
                )
            } catch (ex: NumberFormatException) {
                null
            }
        }
    }
}