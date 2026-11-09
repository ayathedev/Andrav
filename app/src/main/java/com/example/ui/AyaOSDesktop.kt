package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.model.AppType
import com.example.ui.apps.AppHubApp
import com.example.ui.apps.CalculatorApp
import com.example.ui.apps.ChromeBrowserApp
import com.example.ui.apps.FilesManagerApp
import com.example.ui.apps.NotesApp
import com.example.ui.apps.SettingsApp
import com.example.ui.apps.TerminalApp
import com.example.ui.launcher.EverythingLauncherDrawer
import com.example.ui.overlays.DesktopContextMenu
import com.example.ui.overlays.QuickSettingsTray
import com.example.ui.taskbar.TaskbarShelf
import com.example.ui.wallpaper.WallpaperCanvas
import com.example.ui.window.WindowFrame
import com.example.viewmodel.DesktopViewModel

@Composable
fun AyaOSDesktop(
    viewModel: DesktopViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val windows by viewModel.windows.collectAsState()
    val activeWindowId by viewModel.activeWindowId.collectAsState()
    val isDrawerOpen by viewModel.isDrawerOpen.collectAsState()
    val isQuickSettingsOpen by viewModel.isQuickSettingsOpen.collectAsState()
    val drawerSearchQuery by viewModel.drawerSearchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val wallpaperStyle by viewModel.wallpaperStyle.collectAsState()
    val taskbarAlignment by viewModel.taskbarAlignment.collectAsState()
    val isTaskbarAutohide by viewModel.taskbarAutohide.collectAsState()
    val stickyNotes by viewModel.stickyNotes.collectAsState()
    val clockTimeString by viewModel.clockTimeString.collectAsState()
    val batteryLevel by viewModel.batteryLevel.collectAsState()
    val isCharging by viewModel.isCharging.collectAsState()
    val isWifiConnected by viewModel.isWifiConnected.collectAsState()
    val isBluetoothConnected by viewModel.isBluetoothConnected.collectAsState()
    val isNightLightOn by viewModel.isNightLightOn.collectAsState()
    val isDndOn by viewModel.isDndOn.collectAsState()
    val brightness by viewModel.brightness.collectAsState()
    val volume by viewModel.volume.collectAsState()
    val showContextMenu by viewModel.showContextMenu.collectAsState()
    val contextMenuOffset by viewModel.contextMenuOffset.collectAsState()
    val isDefaultLauncher by viewModel.isDefaultLauncher.collectAsState()
    val allApps by viewModel.allApps.collectAsState()

    val pinnedApps = allApps.filter { it.isPinned }

    Box(modifier = modifier.fillMaxSize()) {
        // 1. Pristine Desktop Wallpaper Canvas with Glowing Waves & Typography
        WallpaperCanvas(
            style = wallpaperStyle,
            onLongPress = { offset -> viewModel.openContextMenu(offset) },
            onClick = {
                if (isDrawerOpen) viewModel.toggleDrawer()
                if (isQuickSettingsOpen) viewModel.toggleQuickSettings()
                if (showContextMenu) viewModel.closeContextMenu()
            }
        )

        // 2. Floating Multi-Window Manager Canvas
        val sortedWindows = windows.sortedBy { it.zIndex }
        sortedWindows.forEach { win ->
            val isActive = win.id == activeWindowId

            WindowFrame(
                window = win,
                isActive = isActive,
                onFocus = { viewModel.bringToFront(win.id) },
                onDrag = { delta -> viewModel.dragWindow(win.id, delta) },
                onResize = { delta -> viewModel.resizeWindow(win.id, delta) },
                onMinimize = { viewModel.minimizeWindow(win.id) },
                onMaximizeToggle = { viewModel.toggleMaximizeWindow(win.id) },
                onSnapLeft = { viewModel.snapWindowLeft(win.id) },
                onSnapRight = { viewModel.snapWindowRight(win.id) },
                onClose = { viewModel.closeWindow(win.id) }
            ) {
                // Application Window Router Content
                when (win.appType) {
                    AppType.BROWSER, AppType.CUSTOM_WEB -> {
                        ChromeBrowserApp(initialUrl = win.extraData)
                    }

                    AppType.FILES -> {
                        FilesManagerApp()
                    }

                    AppType.TERMINAL -> {
                        TerminalApp()
                    }

                    AppType.SETTINGS -> {
                        SettingsApp(
                            wallpaperStyle = wallpaperStyle,
                            taskbarAlignment = taskbarAlignment,
                            isTaskbarAutohide = isTaskbarAutohide,
                            isDefaultLauncher = isDefaultLauncher,
                            onWallpaperChange = { viewModel.setWallpaperStyle(it) },
                            onTaskbarAlignmentChange = { viewModel.setTaskbarAlignment(it) },
                            onTaskbarAutohideToggle = { viewModel.toggleTaskbarAutohide() },
                            onLaunchHomeSettings = { ctx -> viewModel.launchHomeSettings(ctx) }
                        )
                    }

                    AppType.CALCULATOR -> {
                        CalculatorApp()
                    }

                    AppType.NOTES -> {
                        NotesApp(
                            notes = stickyNotes,
                            onAddNote = { title, content, color -> viewModel.addStickyNote(title, content, color) },
                            onDeleteNote = { id -> viewModel.deleteStickyNote(id) }
                        )
                    }

                    AppType.APP_HUB -> {
                        AppHubApp(
                            allApps = allApps,
                            onLaunchApp = { app -> viewModel.openApp(app) }
                        )
                    }

                    else -> {
                        ChromeBrowserApp(initialUrl = win.extraData)
                    }
                }
            }
        }

        // 3. ChromeOS Everything App Launcher Overlay
        EverythingLauncherDrawer(
            isOpen = isDrawerOpen,
            searchQuery = drawerSearchQuery,
            selectedCategory = selectedCategory,
            appsList = allApps,
            onSearchQueryChange = { viewModel.setDrawerSearchQuery(it) },
            onCategorySelect = { viewModel.setSelectedCategory(it) },
            onAppLaunch = { app -> viewModel.openApp(app) },
            onTogglePin = { appId -> viewModel.togglePinApp(appId) },
            onDismiss = { viewModel.toggleDrawer() }
        )

        // 4. Quick Settings Tray Overlay
        QuickSettingsTray(
            isOpen = isQuickSettingsOpen,
            isWifiOn = isWifiConnected,
            isBluetoothOn = isBluetoothConnected,
            isNightLightOn = isNightLightOn,
            isDndOn = isDndOn,
            brightness = brightness,
            volume = volume,
            isDefaultLauncher = isDefaultLauncher,
            onWifiToggle = { viewModel.toggleWifi() },
            onBluetoothToggle = { viewModel.toggleBluetooth() },
            onNightLightToggle = { viewModel.toggleNightLight() },
            onDndToggle = { viewModel.toggleDnd() },
            onBrightnessChange = { viewModel.setBrightness(it) },
            onVolumeChange = { viewModel.setVolume(it) },
            onOpenHomeSettings = { viewModel.launchHomeSettings(context) },
            onDismiss = { viewModel.toggleQuickSettings() }
        )

        // 5. Desktop Context Menu Overlay
        DesktopContextMenu(
            isOpen = showContextMenu,
            offset = contextMenuOffset,
            onDismiss = { viewModel.closeContextMenu() },
            onChangeWallpaperClick = {
                val settingsApp = allApps.first { it.appType == AppType.SETTINGS }
                viewModel.openApp(settingsApp)
            },
            onNewNoteClick = {
                val notesApp = allApps.first { it.appType == AppType.NOTES }
                viewModel.openApp(notesApp)
            },
            onOpenTerminalClick = {
                val termApp = allApps.first { it.appType == AppType.TERMINAL }
                viewModel.openApp(termApp)
            },
            onSetDefaultLauncherClick = {
                viewModel.launchHomeSettings(context)
            },
            onSetShelfAlignment = { align -> viewModel.setTaskbarAlignment(align) }
        )

        // 6. Taskbar / Shelf (Bottom)
        TaskbarShelf(
            pinnedApps = pinnedApps,
            openWindows = windows,
            activeWindowId = activeWindowId,
            clockTime = clockTimeString,
            batteryLevel = batteryLevel,
            isCharging = isCharging,
            isWifiOn = isWifiConnected,
            isBluetoothOn = isBluetoothConnected,
            alignment = taskbarAlignment,
            isAutohide = isTaskbarAutohide,
            isDrawerOpen = isDrawerOpen,
            onEverythingButtonClick = { viewModel.toggleDrawer() },
            onAppClick = { app -> viewModel.openApp(app) },
            onWindowIconClick = { winId -> viewModel.toggleMinimizeWindow(winId) },
            onSystemTrayClick = { viewModel.toggleQuickSettings() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
