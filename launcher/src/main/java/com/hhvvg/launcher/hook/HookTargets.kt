package com.hhvvg.launcher.hook

import com.hhvvg.launcher.AppFilter
import com.hhvvg.launcher.CellLayout
import com.hhvvg.launcher.DeviceProfile
import com.hhvvg.launcher.InvariantDeviceProfile
import com.hhvvg.launcher.Launcher
import com.hhvvg.launcher.Workspace
import com.hhvvg.launcher.component.Component
import com.hhvvg.launcher.folder.Folder
import com.hhvvg.launcher.folder.FolderIcon
import com.hhvvg.launcher.folder.PreviewBackground
import com.hhvvg.launcher.icon.BubbleTextView
import com.hhvvg.launcher.icon.DotRenderer
import com.hhvvg.launcher.icon.FastBitmapDrawable
import com.hhvvg.launcher.icon.IconCache
import com.hhvvg.launcher.icon.LauncherActivityCachingLogic
import com.hhvvg.launcher.icon.LauncherIconProvider
import com.hhvvg.launcher.icon.LauncherIcons
import com.hhvvg.launcher.model.AllAppsList
import com.hhvvg.launcher.quickstep.RecentsView
import com.hhvvg.launcher.states.SpringLoadedState
import com.hhvvg.launcher.view.OptionsPopupView
import kotlin.reflect.KClass

val components: Array<KClass<out Component>> = arrayOf(
    Launcher::class,
    DeviceProfile::class,
    FastBitmapDrawable::class,
    IconCache::class,
    LauncherActivityCachingLogic::class,
    BubbleTextView::class,
    FolderIcon::class,
    PreviewBackground::class,
    LauncherIconProvider::class,
    LauncherIcons::class,
    DotRenderer::class,
    CellLayout::class,
    InvariantDeviceProfile.GridOption::class,
    Folder::class,
    OptionsPopupView::class,
    AppFilter::class,
    AllAppsList::class,
    RecentsView::class,
    SpringLoadedState::class,
    Workspace::class
)