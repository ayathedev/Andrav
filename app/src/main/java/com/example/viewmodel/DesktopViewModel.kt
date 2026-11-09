package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AppCategory
import com.example.model.AppMetaData
import com.example.model.AppType
import com.example.model.DesktopWindow
import com.example.model.StickyNote
import com.example.model.TaskbarAlignment
import com.example.model.WallpaperStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DesktopViewModel(application: Application) : AndroidViewModel(application) {

    private val _windows = MutableStateFlow<List<DesktopWindow>>(emptyList())
    val windows: StateFlow<List<DesktopWindow>> = _windows.asStateFlow()

    private val _activeWindowId = MutableStateFlow<String?>(null)
    val activeWindowId: StateFlow<String?> = _activeWindowId.asStateFlow()

    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

    private val _isQuickSettingsOpen = MutableStateFlow(false)
    val isQuickSettingsOpen: StateFlow<Boolean> = _isQuickSettingsOpen.asStateFlow()

    private val _drawerSearchQuery = MutableStateFlow("")
    val drawerSearchQuery: StateFlow<String> = _drawerSearchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(AppCategory.ALL)
    val selectedCategory: StateFlow<AppCategory> = _selectedCategory.asStateFlow()

    private val _wallpaperStyle = MutableStateFlow(WallpaperStyle.FLUID_DARK_WAVES)
    val wallpaperStyle: StateFlow<WallpaperStyle> = _wallpaperStyle.asStateFlow()

    private val _taskbarAlignment = MutableStateFlow(TaskbarAlignment.CENTER)
    val taskbarAlignment: StateFlow<TaskbarAlignment> = _taskbarAlignment.asStateFlow()

    private val _taskbarAutohide = MutableStateFlow(false)
    val taskbarAutohide: StateFlow<Boolean> = _taskbarAutohide.asStateFlow()

    private val _stickyNotes = MutableStateFlow<List<StickyNote>>(
        listOf(
            StickyNote(
                id = "note_1",
                title = "Welcome to Aya OS",
                content = "ChromeOS-inspired tablet launcher. Use the Everything button to open your apps drawer!",
                colorHex = 0xFF00F2FE
            ),
            StickyNote(
                id = "note_2",
                title = "Crostini Terminal",
                content = "Try typing 'neofetch' or 'help' in the Crostini Terminal application.",
                colorHex = 0xFFFF007F
            )
        )
    )
    val stickyNotes: StateFlow<List<StickyNote>> = _stickyNotes.asStateFlow()

    // Clock state updating every second
    private val _clockTimeString = MutableStateFlow(getCurrentTime())
    val clockTimeString: StateFlow<String> = _clockTimeString.asStateFlow()

    // System hardware states
    private val _batteryLevel = MutableStateFlow(92)
    val batteryLevel: StateFlow<Int> = _batteryLevel.asStateFlow()

    private val _isCharging = MutableStateFlow(false)
    val isCharging: StateFlow<Boolean> = _isCharging.asStateFlow()

    private val _isWifiConnected = MutableStateFlow(true)
    val isWifiConnected: StateFlow<Boolean> = _isWifiConnected.asStateFlow()

    private val _isBluetoothConnected = MutableStateFlow(true)
    val isBluetoothConnected: StateFlow<Boolean> = _isBluetoothConnected.asStateFlow()

    private val _isNightLightOn = MutableStateFlow(false)
    val isNightLightOn: StateFlow<Boolean> = _isNightLightOn.asStateFlow()

    private val _isDndOn = MutableStateFlow(false)
    val isDndOn: StateFlow<Boolean> = _isDndOn.asStateFlow()

    private val _brightness = MutableStateFlow(0.85f)
    val brightness: StateFlow<Float> = _brightness.asStateFlow()

    private val _volume = MutableStateFlow(0.70f)
    val volume: StateFlow<Float> = _volume.asStateFlow()

    // Desktop Context Menu
    private val _showContextMenu = MutableStateFlow(false)
    val showContextMenu: StateFlow<Boolean> = _showContextMenu.asStateFlow()

    private val _contextMenuOffset = MutableStateFlow(Offset.Zero)
    val contextMenuOffset: StateFlow<Offset> = _contextMenuOffset.asStateFlow()

    // Default Launcher Status
    private val _isDefaultLauncher = MutableStateFlow(false)
    val isDefaultLauncher: StateFlow<Boolean> = _isDefaultLauncher.asStateFlow()

    // App List
    private val _allApps = MutableStateFlow<List<AppMetaData>>(emptyList())
    val allApps: StateFlow<List<AppMetaData>> = _allApps.asStateFlow()

    private var zIndexCounter = 1f

    init {
        startClockTimer()
        updateBatteryState()
        loadAllApps()
        checkDefaultLauncher()

        // Open Welcome window by default on startup
        openApp(getBuiltInApps().first { it.appType == AppType.BROWSER })
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("h:mm:ss a", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun startClockTimer() {
        viewModelScope.launch {
            while (true) {
                _clockTimeString.value = getCurrentTime()
                delay(1000)
            }
        }
    }

    fun updateBatteryState() {
        try {
            val context = getApplication<Application>()
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, intentFilter)
            batteryStatus?.let {
                val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                if (level >= 0 && scale > 0) {
                    _batteryLevel.value = (level * 100 / scale.toFloat()).toInt()
                }
                val status = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                _isCharging.value = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL
            }
        } catch (e: Exception) {
            // Default level if check fails
        }
    }

    fun checkDefaultLauncher() {
        try {
            val context = getApplication<Application>()
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
            }
            val resolveInfo = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            _isDefaultLauncher.value = resolveInfo?.activityInfo?.packageName == context.packageName
        } catch (e: Exception) {
            _isDefaultLauncher.value = false
        }
    }

    fun launchHomeSettings(context: Context) {
        try {
            val intent = Intent(android.provider.Settings.ACTION_HOME_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(android.provider.Settings.ACTION_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    private fun getBuiltInApps(): List<AppMetaData> {
        return listOf(
            AppMetaData(
                id = "app_browser",
                title = "Chrome",
                icon = Icons.Default.Language,
                category = AppCategory.BUILT_IN,
                appType = AppType.BROWSER,
                description = "Multi-tab Web Browser with bookmarks",
                isPinned = true
            ),
            AppMetaData(
                id = "app_files",
                title = "Files",
                icon = Icons.Default.Folder,
                category = AppCategory.BUILT_IN,
                appType = AppType.FILES,
                description = "Dual-pane storage file manager",
                isPinned = true
            ),
            AppMetaData(
                id = "app_terminal",
                title = "Terminal",
                icon = Icons.Default.Terminal,
                category = AppCategory.BUILT_IN,
                appType = AppType.TERMINAL,
                description = "Crostini Linux terminal environment",
                isPinned = true
            ),
            AppMetaData(
                id = "app_settings",
                title = "Settings",
                icon = Icons.Default.Settings,
                category = AppCategory.BUILT_IN,
                appType = AppType.SETTINGS,
                description = "System preferences & wallpaper gallery",
                isPinned = true
            ),
            AppMetaData(
                id = "app_calculator",
                title = "Calculator",
                icon = Icons.Default.Calculate,
                category = AppCategory.PRODUCTIVITY,
                appType = AppType.CALCULATOR,
                description = "Material desktop calculator",
                isPinned = true
            ),
            AppMetaData(
                id = "app_notes",
                title = "Notes",
                icon = Icons.Default.Notes,
                category = AppCategory.PRODUCTIVITY,
                appType = AppType.NOTES,
                description = "Sticky notes board",
                isPinned = true
            ),
            AppMetaData(
                id = "app_hub",
                title = "App Hub",
                icon = Icons.Default.Shop,
                category = AppCategory.PRODUCTIVITY,
                appType = AppType.APP_HUB,
                description = "Installed Android apps & Web shortcuts",
                isPinned = true
            )
        )
    }

    private fun getWebShortcutApps(): List<AppMetaData> {
        return listOf(
            AppMetaData(
                id = "web_figma",
                title = "Figma",
                icon = Icons.Default.Language,
                category = AppCategory.WEB_APPS,
                appType = AppType.CUSTOM_WEB,
                packageOrUrl = "https://www.figma.com",
                description = "Interface Design Tool",
                isPinned = false
            ),
            AppMetaData(
                id = "web_vscode",
                title = "VS Code",
                icon = Icons.Default.Language,
                category = AppCategory.WEB_APPS,
                appType = AppType.CUSTOM_WEB,
                packageOrUrl = "https://vscode.dev",
                description = "Web Code Editor",
                isPinned = false
            ),
            AppMetaData(
                id = "web_spotify",
                title = "Spotify",
                icon = Icons.Default.Language,
                category = AppCategory.WEB_APPS,
                appType = AppType.CUSTOM_WEB,
                packageOrUrl = "https://open.spotify.com",
                description = "Web Music Player",
                isPinned = false
            ),
            AppMetaData(
                id = "web_discord",
                title = "Discord",
                icon = Icons.Default.Language,
                category = AppCategory.WEB_APPS,
                appType = AppType.CUSTOM_WEB,
                packageOrUrl = "https://discord.com/app",
                description = "Community Chat & Voice",
                isPinned = false
            ),
            AppMetaData(
                id = "web_gemini",
                title = "Gemini AI",
                icon = Icons.Default.Language,
                category = AppCategory.WEB_APPS,
                appType = AppType.CUSTOM_WEB,
                packageOrUrl = "https://gemini.google.com",
                description = "Google AI Assistant",
                isPinned = false
            )
        )
    }

    fun loadAllApps() {
        val list = mutableListOf<AppMetaData>()
        list.addAll(getBuiltInApps())
        list.addAll(getWebShortcutApps())

        // Also query installed Android apps
        try {
            val pm = getApplication<Application>().packageManager
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val apps = pm.queryIntentActivities(intent, 0)
            for (app in apps) {
                val pkg = app.activityInfo.packageName
                if (pkg != getApplication<Application>().packageName) {
                    val label = app.loadLabel(pm).toString()
                    list.add(
                        AppMetaData(
                            id = "android_$pkg",
                            title = label,
                            icon = Icons.Default.Android,
                            category = AppCategory.ANDROID_APPS,
                            appType = AppType.ANDROID_APP,
                            packageOrUrl = pkg,
                            description = pkg,
                            isPinned = false
                        )
                    )
                }
            }
        } catch (e: Exception) {
            // Ignore if query permission restricted
        }

        _allApps.value = list
    }

    // Toggle Pin/Unpin
    fun togglePinApp(appId: String) {
        _allApps.update { current ->
            current.map { app ->
                if (app.id == appId) app.copy(isPinned = !app.isPinned) else app
            }
        }
    }

    // Window Management
    fun openApp(app: AppMetaData, extraUrlOrPkg: String? = null) {
        _isDrawerOpen.value = false

        // Check if Android App package
        if (app.appType == AppType.ANDROID_APP) {
            val context = getApplication<Application>()
            val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageOrUrl)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
                return
            }
        }

        val existingWindow = _windows.value.find { it.id == app.id }
        if (existingWindow != null) {
            // Restore and bring to front
            bringToFront(existingWindow.id)
            _windows.update { list ->
                list.map {
                    if (it.id == existingWindow.id) {
                        it.copy(isMinimized = false, extraData = extraUrlOrPkg ?: it.extraData)
                    } else it
                }
            }
        } else {
            // Spawn new window
            zIndexCounter += 1f
            val windowCount = _windows.value.size
            val initialX = 120f + (windowCount % 5) * 40f
            val initialY = 70f + (windowCount % 5) * 30f

            val defaultSize = when (app.appType) {
                AppType.CALCULATOR -> DpSize(380.dp, 520.dp)
                AppType.NOTES -> DpSize(620.dp, 460.dp)
                AppType.TERMINAL -> DpSize(720.dp, 450.dp)
                else -> DpSize(820.dp, 540.dp)
            }

            val newWin = DesktopWindow(
                id = app.id,
                appType = if (app.appType == AppType.CUSTOM_WEB) AppType.BROWSER else app.appType,
                title = app.title,
                icon = app.icon,
                offset = Offset(initialX, initialY),
                size = defaultSize,
                zIndex = zIndexCounter,
                extraData = extraUrlOrPkg ?: app.packageOrUrl
            )

            _windows.update { it + newWin }
            _activeWindowId.value = newWin.id
        }
    }

    fun openWebUrlInBrowser(url: String, title: String = "Web") {
        val browserApp = getBuiltInApps().first { it.appType == AppType.BROWSER }
        openApp(browserApp, extraUrlOrPkg = url)
    }

    fun bringToFront(windowId: String) {
        zIndexCounter += 1f
        _activeWindowId.value = windowId
        _windows.update { list ->
            list.map {
                if (it.id == windowId) it.copy(zIndex = zIndexCounter) else it
            }
        }
    }

    fun toggleMinimizeWindow(windowId: String) {
        _windows.update { list ->
            list.map {
                if (it.id == windowId) {
                    val nextMinimized = !it.isMinimized
                    if (!nextMinimized) {
                        _activeWindowId.value = windowId
                        zIndexCounter += 1f
                        it.copy(isMinimized = false, zIndex = zIndexCounter)
                    } else {
                        if (_activeWindowId.value == windowId) _activeWindowId.value = null
                        it.copy(isMinimized = true)
                    }
                } else it
            }
        }
    }

    fun minimizeWindow(windowId: String) {
        _windows.update { list ->
            list.map {
                if (it.id == windowId) it.copy(isMinimized = true) else it
            }
        }
        if (_activeWindowId.value == windowId) _activeWindowId.value = null
    }

    fun toggleMaximizeWindow(windowId: String) {
        bringToFront(windowId)
        _windows.update { list ->
            list.map {
                if (it.id == windowId) {
                    it.copy(
                        isMaximized = !it.isMaximized,
                        isSnappedLeft = false,
                        isSnappedRight = false
                    )
                } else it
            }
        }
    }

    fun snapWindowLeft(windowId: String) {
        bringToFront(windowId)
        _windows.update { list ->
            list.map {
                if (it.id == windowId) {
                    it.copy(
                        isSnappedLeft = true,
                        isSnappedRight = false,
                        isMaximized = false
                    )
                } else it
            }
        }
    }

    fun snapWindowRight(windowId: String) {
        bringToFront(windowId)
        _windows.update { list ->
            list.map {
                if (it.id == windowId) {
                    it.copy(
                        isSnappedRight = true,
                        isSnappedLeft = false,
                        isMaximized = false
                    )
                } else it
            }
        }
    }

    fun closeWindow(windowId: String) {
        _windows.update { list -> list.filterNot { it.id == windowId } }
        if (_activeWindowId.value == windowId) {
            _activeWindowId.value = _windows.value.maxByOrNull { it.zIndex }?.id
        }
    }

    fun dragWindow(windowId: String, delta: Offset) {
        _windows.update { list ->
            list.map {
                if (it.id == windowId && !it.isMaximized && !it.isSnappedLeft && !it.isSnappedRight) {
                    val newX = (it.offset.x + delta.x).coerceAtLeast(0f)
                    val newY = (it.offset.y + delta.y).coerceAtLeast(0f)
                    it.copy(offset = Offset(newX, newY))
                } else it
            }
        }
    }

    fun resizeWindow(windowId: String, delta: Offset) {
        _windows.update { list ->
            list.map {
                if (it.id == windowId && !it.isMaximized) {
                    val newW = (it.size.width.value + delta.x / 2.5f).coerceAtLeast(320f)
                    val newH = (it.size.height.value + delta.y / 2.5f).coerceAtLeast(240f)
                    it.copy(size = DpSize(newW.dp, newH.dp))
                } else it
            }
        }
    }

    // Sticky Notes
    fun addStickyNote(title: String, content: String, colorHex: Long) {
        val newNote = StickyNote(
            id = "note_${System.currentTimeMillis()}",
            title = title.ifBlank { "Untitled Note" },
            content = content,
            colorHex = colorHex
        )
        _stickyNotes.update { it + newNote }
    }

    fun deleteStickyNote(id: String) {
        _stickyNotes.update { list -> list.filterNot { it.id == id } }
    }

    // UI Toggles & Setters
    fun toggleDrawer() {
        _isDrawerOpen.value = !_isDrawerOpen.value
        if (_isDrawerOpen.value) _isQuickSettingsOpen.value = false
    }

    fun setDrawerSearchQuery(query: String) {
        _drawerSearchQuery.value = query
    }

    fun setSelectedCategory(category: AppCategory) {
        _selectedCategory.value = category
    }

    fun toggleQuickSettings() {
        _isQuickSettingsOpen.value = !_isQuickSettingsOpen.value
        if (_isQuickSettingsOpen.value) _isDrawerOpen.value = false
    }

    fun setWallpaperStyle(style: WallpaperStyle) {
        _wallpaperStyle.value = style
    }

    fun setTaskbarAlignment(alignment: TaskbarAlignment) {
        _taskbarAlignment.value = alignment
    }

    fun toggleTaskbarAutohide() {
        _taskbarAutohide.value = !_taskbarAutohide.value
    }

    fun toggleWifi() {
        _isWifiConnected.value = !_isWifiConnected.value
    }

    fun toggleBluetooth() {
        _isBluetoothConnected.value = !_isBluetoothConnected.value
    }

    fun toggleNightLight() {
        _isNightLightOn.value = !_isNightLightOn.value
    }

    fun toggleDnd() {
        _isDndOn.value = !_isDndOn.value
    }

    fun setBrightness(valF: Float) {
        _brightness.value = valF
    }

    fun setVolume(valF: Float) {
        _volume.value = valF
    }

    fun openContextMenu(offset: Offset) {
        _contextMenuOffset.value = offset
        _showContextMenu.value = true
    }

    fun closeContextMenu() {
        _showContextMenu.value = false
    }
}
