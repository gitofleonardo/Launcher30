package com.hhvvg.launcher.service

import android.content.ComponentName
import android.content.Context
import android.os.RemoteCallbackList
import android.os.UserHandle
import com.hhvvg.launcher.ILauncherCallbacks
import com.hhvvg.launcher.ILauncherService
import com.hhvvg.launcher.repository.AppLabelRepository
import com.hhvvg.launcher.repository.CommonRepository
import com.hhvvg.launcher.repository.PrivacyAppsRepository
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
    private val privacyAppsRepository by lazy {
        PrivacyAppsRepository(context)
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

    override fun setAdaptiveIconEnable(enabled: Boolean) {
        commonRepository.setAdaptiveIconEnable(enabled)
        broadcast {
            it.onIconPackProviderChanged(commonRepository.getIconPackProvider())
        }
    }

    override fun isAdaptiveIconEnable(): Boolean {
        return commonRepository.isAdaptiveIconEnable()
    }

    override fun forceReloadLauncher() {
        broadcast {
            it.onLauncherReload()
        }
    }

    override fun setIconTextVisible(visible: Boolean) {
        commonRepository.setIconTextVisible(visible)
        broadcast {
            it.onIconTextVisibilityChanged(visible)
        }
    }

    override fun isIconTextVisible(): Boolean {
        return commonRepository.isIconTextVisible()
    }

    override fun setDrawNotificationCount(enable: Boolean) {
        commonRepository.setDrawNotificationCount(enable)
        broadcast {
            it.onDrawNotificationCountChanged(enable)
        }
    }

    override fun isDrawNotificationCount(): Boolean {
        return commonRepository.isDrawNotificationCount()
    }

    override fun isSpringLoadedBgEnable(): Boolean {
        return commonRepository.isSpringLoadedBgEnable()
    }

    override fun setSpringLoadedBgEnable(enable: Boolean) {
        commonRepository.setSpringLoadedBgEnable(enable)
        broadcast {
            it.onSpringLoadedBgEnable(enable)
        }
    }

    override fun setQsbEnable(enable: Boolean) {
        commonRepository.setQsbEnable(enable)
        broadcast {
            it.onQsbStateChanged(enable)
        }
    }

    override fun isQsbEnable(): Boolean {
        return commonRepository.isQsbEnable()
    }

    override fun setOpenedFolderCenter(center: Boolean) {
        commonRepository.setOpenedFolderCenter(center)
        broadcast {
            it.onOpenedFolderCenterChanged(center)
        }
    }

    override fun isOpenedFolderCenter(): Boolean {
        return commonRepository.isOpenedFolderCenter()
    }

    override fun changePrivacyItem(cn: ComponentName, isPrivacy: Boolean) {
        privacyAppsRepository.putPrivacyApp(cn, isPrivacy)
        broadcast {
            it.onPrivacyItemChange(cn, isPrivacy)
        }
    }

    override fun getPrivacyItems(): List<ComponentName> {
        return privacyAppsRepository.getPrivacyItems()
    }

    override fun setBiometricPrivacyApps(useBiometric: Boolean) {
        commonRepository.setUseBiometricPrivacyApps(useBiometric)
    }

    override fun useBiometricPrivacyApps(): Boolean {
        return commonRepository.useBiometricPrivacyApps()
    }

    override fun setPrivacyHiddenFromRecents(hidden: Boolean) {
        commonRepository.setPrivacyAppsHiddenFromRecents(hidden)
    }

    override fun isPrivacyHiddenFromRecents(): Boolean {
        return commonRepository.isPrivacyAppsHiddenFromRecents()
    }

    override fun setUseCustomSpringLoadedEffect(use: Boolean) {
        commonRepository.setUseCustomSpringLoadedEffect(use)
        broadcast {
            it.onSetUseCustomSpringLoadedEffect(use)
        }
    }

    override fun isUseCustomSpringLoadedEffect(): Boolean {
        return commonRepository.isUseCustomSpringLoadedEffect()
    }

    override fun getIconScale(): Float {
        return commonRepository.getIconScale()
    }

    override fun setIconScale(scale: Float) {
        commonRepository.setIconScale(scale)
        broadcast {
            it.onIconScaleChanged(scale)
        }
    }

    override fun getIconTextScale(): Float {
        return commonRepository.getIconTextScale()
    }

    override fun setIconTextScale(scale: Float) {
        commonRepository.setIconTextScale(scale)
        broadcast {
            it.onIconTextScaleChanged(scale)
        }
    }

    override fun setIconDrawablePaddingScale(scale: Float) {
        commonRepository.setIconDrawablePaddingScale(scale)
        broadcast {
            it.onIconDrawablePaddingScaleChanged(scale)
        }
    }

    override fun getIconDrawablePaddingScale(): Float {
        return commonRepository.getIconDrawablePaddingScale()
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
