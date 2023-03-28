package com.hhvvg.launcher.service

import android.content.ComponentName
import android.content.Context
import android.os.Binder
import android.os.IBinder
import android.os.IBinder.DeathRecipient
import android.os.RemoteCallbackList
import android.os.UserHandle
import com.hhvvg.launcher.ILauncherCallbacks
import com.hhvvg.launcher.ILauncherService
import com.hhvvg.launcher.repository.AppLabelRepository
import com.hhvvg.launcher.repository.CommonRepository
import com.hhvvg.launcher.utils.Logger
import com.kaisar.xservicemanager.XServiceManager

/**
 * @author hhvvg
 */
class LauncherService(private val context: Context) : ILauncherService.Stub() {
    private val callbackList = RemoteCallbackList<ILauncherCallbacks>()

    private val appLabelRepository by lazy {
        AppLabelRepository(context)
    }
    private val commonRepository by lazy {
        CommonRepository(context)
    }

    override fun registerLauncherCallbacks(callbacks: ILauncherCallbacks) {
        callbackList.register(callbacks)
    }

    override fun unregisterLauncherCallbacks(callbacks: ILauncherCallbacks) {
        callbackList.unregister(callbacks)
    }

    override fun setComponentLabel(cn: ComponentName, user: UserHandle, title: CharSequence?) {
        appLabelRepository.setLabelForComponent(cn, user, title)
        broadcast {
            it.onComponentLabelUpdated(cn, user, title)
        }
    }

    override fun getComponentLabel(cn: ComponentName, user: UserHandle): CharSequence? {
        return appLabelRepository.getLabelForComponent(cn, user)
    }

    override fun setClickEffectEnable(enabled: Boolean) {
        commonRepository.setIconClickEffectEnable(enabled)
        broadcast {
            it.onIconClickEffectEnable(enabled)
        }
    }

    override fun isClickEffectEnable(): Boolean {
        return commonRepository.isIconClickEffectEnable()
    }

    override fun setDotParamsColor(color: Int) {
        commonRepository.setDotParamsColor(color)
        broadcast {
            it.onDotParamsColorChanged(color)
        }
    }

    override fun restoreDotParamsColor() {
        commonRepository.removeDotParamsColor()
        broadcast {
            it.onDotParamsColorRestored()
        }
    }

    override fun getDotParamsColor(): Int {
        return commonRepository.getDotParamsColor()
    }

    override fun resetAppFavorites(cn: ComponentName, user: UserHandle) {
        appLabelRepository.clearLabelForComponent(cn, user)
        broadcast {
            it.onComponentLabelUpdated(cn, user, "")
        }
    }

    override fun setIconPackProvider(provider: String?) {
        commonRepository.setIconPackProvider(provider)
        broadcast {
            it.onIconPackProviderChanged(provider)
        }
    }

    override fun getIconPackProvider(): String? {
        return commonRepository.getIconPackProvider()
    }

    private fun broadcast(action: (ILauncherCallbacks) -> Unit) {
        var i = callbackList.beginBroadcast()
        while (i > 0) {
            --i
            try {
                action.invoke(callbackList.getBroadcastItem(i))
            } catch (e : Exception) {
                Logger.log("Failed to broadcast callback to launcher", e)
            }
        }
        callbackList.finishBroadcast()
    }

    companion object {
        private const val SERVICE_NAME = "ILauncherService"

        @JvmStatic
        fun initLauncherService() {
            XServiceManager.initForSystemServer()
            XServiceManager.registerService(SERVICE_NAME) {
                LauncherService(it)
            }
        }

        @JvmStatic
        fun getLauncherService(): ILauncherService {
            val binder = XServiceManager.getService(SERVICE_NAME)
            return asInterface(binder)
        }
    }
}
