package com.hhvvg.launcher3customizer.model

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Xml
import androidx.core.content.res.ResourcesCompat
import com.hhvvg.launcher3customizer.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.FileNotFoundException

class IconPacksModel {
    suspend fun loadAllIconPacks(context: Context): List<IconPackItem> = withContext(context = Dispatchers.IO){
        val packageManager = context.packageManager
        ALL_ICON_PACK_ACTIONS.map {
            packageManager.queryIntentActivities(Intent(it), 0)
        }.flatten().map {
            IconPackItem(it.activityInfo.packageName, it.loadIcon(packageManager), it.loadLabel(packageManager))
        }.distinctBy {
            it.packageName
        }
    }

    suspend fun loadAllIconResources(context: Context, iconPackPkg: String): List<IconItem> = withContext(context = Dispatchers.IO) {
        getIconDrawables(context, iconPackPkg) ?: getAppFilter(context, iconPackPkg)
    }

    fun loadIconDrawable(iconResource: IconItem): IconDrawableItem? {
        val id = iconResource.resources.getIdentifier(iconResource.resourceName,
            "drawable", iconResource.iconPackPkg)
        if (id < 0) {
            return null
        }
        val drawable = try {
            ResourcesCompat.getDrawable(iconResource.resources, id, null)
        } catch (e: Exception) {
            null
        }
        return if (drawable == null) {
            null
        } else {
            IconDrawableItem(drawable, iconResource.resourceName)
        }
    }

    private fun getAppFilter(
        context: Context,
        iconPackPackageName: String,
        componentNameFilter: ((componentName: String) -> Boolean) = { true }
    ): List<IconComponentItem> {
        val packageContext = createPackageContext(context, iconPackPackageName)
        val packageResources = createPackageResources(context, iconPackPackageName)
        val appFilterResId = packageResources.getIdentifier(
            APPFILTER,
            "xml",
            iconPackPackageName
        )
        val parser = if(appFilterResId == 0) {
            try {
                packageContext.assets.open(APPFILTER_XML)
            }catch (e: FileNotFoundException){
                return emptyList()
            }.run {
                val parser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(this, null)
                parser
            }
        }else{
            packageResources.getXml(appFilterResId)
        }
        return try {
            parser.parseAppFilter(
                iconPackPackageName,
                packageResources,
                componentNameFilter
            )
        }catch (e: XmlPullParserException) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun getIconDrawables(
        context: Context,
        iconPackPackageName: String
    ): List<IconCategoryItem>? {
        val packageContext = createPackageContext(context, iconPackPackageName)
        val packageResources = createPackageResources(context, iconPackPackageName)
        val appFilterResId = packageResources.getIdentifier(
            DRAWABLE,
            "xml",
            iconPackPackageName
        )
        val parser = if(appFilterResId == 0){
            try {
                packageContext.assets.open(DRAWABLE_XML)
            }catch (e: FileNotFoundException){
                return null
            }.run {
                val parser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(this, null)
                parser
            }
        }else{
            packageResources.getXml(appFilterResId)
        }
        return try {
            parser.parseDrawable(iconPackPackageName, packageResources)
        }catch (e: XmlPullParserException) {
            null
        }?.distinctBy { (it.category ?: "") + it.resourceName }
    }

    private fun createPackageContext(context: Context, pkg: String): Context {
        return context.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY)
    }

    private fun createPackageResources(context: Context, pkg: String): Resources {
        return context.packageManager.getResourcesForApplication(pkg)
    }

    private fun XmlPullParser.parseDrawable(
        packageName: String,
        resources: Resources
    ): List<IconCategoryItem> {
        val icons = ArrayList<IconCategoryItem>()
        var category: String? = null
        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG){
                when(name){
                    "category" -> category = getAttributeValue(null, "title")
                    "item" -> {
                        val drawable = getAttributeValue(null, "drawable")
                        if(drawable == null) {
                            next()
                            continue
                        }
                        val identifier = resources.getIdentifier(drawable, "drawable", packageName)
                        if(identifier == 0) {
                            next()
                            continue
                        }
                        icons.add(IconCategoryItem(packageName, resources, drawable, category))
                    }
                }
            }
            next()
        }
        return icons
    }

    private fun XmlPullParser.parseAppFilter(
        packageName: String,
        resources: Resources,
        componentNameFilter: (componentName: String) -> Boolean
    ): List<IconComponentItem> {
        val icons = ArrayList<IconComponentItem>()
        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG && name == "item"){
                val component = getAttributeValue(null, "component")
                val parsedComponent = component?.let {
                    COMPONENT_REGEX.find(component)?.groupValues?.get(1)
                }
                if(parsedComponent != null && componentNameFilter(parsedComponent)){
                    val drawable = getAttributeValue(null, "drawable")
                    if(drawable == null) {
                        next()
                        continue
                    }
                    val identifier = resources.getIdentifier(drawable, "drawable", packageName)
                    if(identifier == 0) {
                        next()
                        continue
                    }
                    ComponentName.unflattenFromString(component)?.let {
                        IconComponentItem(
                            packageName,
                            resources,
                            drawable,
                            it
                        )
                    }?.let {
                        icons.add(
                            it
                        )
                    }
                }
            }
            next()
        }
        return icons
    }

    companion object {
        private const val APPFILTER = "appfilter"
        private const val DRAWABLE = "drawable"
        private const val APPFILTER_XML = "$APPFILTER.xml"
        private const val DRAWABLE_XML = "$DRAWABLE.xml"
        private val COMPONENT_REGEX = "ComponentInfo\\{(.*)\\}".toRegex()
        private val ALL_ICON_PACK_ACTIONS = arrayOf(
            "org.adw.launcher.THEMES",
            "org.adw.launcher.icons.ACTION_PICK_ICON",
            "com.anddoes.launcher.THEME",
            "com.gau.go.launcherex.theme",
            "com.dlto.atom.launcher.THEME",
            "com.phonemetra.turbo.launcher.icons.ACTION_PICK_ICON",
            "com.gridappsinc.launcher.theme.apk_action",
            "ch.deletescape.lawnchair.ICONPACK",
            "com.novalauncher.THEME",
            "home.solo.launcher.free.THEMES",
            "home.solo.launcher.free.ACTION_ICON",
            "com.lge.launcher2.THEME",
            "net.oneplus.launcher.icons.ACTION_PICK_ICON",
            "com.tsf.shell.themes",
            "ginlemon.smartlauncher.THEMES",
            "com.sonymobile.home.ICON_PACK",
            "com.gau.go.launcherex.theme",
            "com.zeroteam.zerolauncher.theme",
            "jp.co.a_tm.android.launcher.icons.ACTION_PICK_ICON",
            "com.vivid.launcher.theme"
        )
    }
}
