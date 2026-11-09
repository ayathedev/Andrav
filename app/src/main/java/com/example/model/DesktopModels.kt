package com.example.model

import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

enum class AppType {
    BROWSER,
    FILES,
    TERMINAL,
    SETTINGS,
    CALCULATOR,
    NOTES,
    APP_HUB,
    CUSTOM_WEB,
    ANDROID_APP
}

enum class AppCategory(val displayName: String) {
    ALL("All Apps"),
    BUILT_IN("Built-in"),
    PRODUCTIVITY("Productivity"),
    WEB_APPS("Web Apps"),
    ANDROID_APPS("Android Apps")
}

data class AppMetaData(
    val id: String,
    val title: String,
    val icon: ImageVector = Icons.Default.Language,
    val category: AppCategory = AppCategory.BUILT_IN,
    val appType: AppType,
    val packageOrUrl: String = "",
    val description: String = "",
    val isPinned: Boolean = true
)

data class DesktopWindow(
    val id: String,
    val appType: AppType,
    val title: String,
    val icon: ImageVector,
    val offset: Offset = Offset(100f, 60f),
    val size: DpSize = DpSize(750.dp, 480.dp),
    val isMinimized: Boolean = false,
    val isMaximized: Boolean = false,
    val isSnappedLeft: Boolean = false,
    val isSnappedRight: Boolean = false,
    val zIndex: Float = 1f,
    val extraData: String = ""
)

data class StickyNote(
    val id: String,
    val title: String,
    val content: String,
    val colorHex: Long = 0xFF00F2FE,
    val updatedAt: Long = System.currentTimeMillis()
)

data class FileItem(
    val id: String,
    val name: String,
    val size: String,
    val dateModified: String,
    val isDirectory: Boolean,
    val iconType: String = "file"
)

enum class WallpaperStyle(val displayName: String) {
    FLUID_DARK_WAVES("Fluid Dark Waves"),
    CYBER_GRID("Cyber Grid"),
    NEON_AURORA("Neon Aurora"),
    OBSIDIAN_NEBULA("Obsidian Nebula")
}

enum class TaskbarAlignment {
    LEFT, CENTER, RIGHT
}
