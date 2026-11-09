package com.example.ui.taskbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppMetaData
import com.example.model.DesktopWindow
import com.example.model.TaskbarAlignment
import com.example.ui.theme.ChromeGlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.TaskbarBg
import com.example.ui.theme.TextPrimary

@Composable
fun TaskbarShelf(
    pinnedApps: List<AppMetaData>,
    openWindows: List<DesktopWindow>,
    activeWindowId: String?,
    clockTime: String,
    batteryLevel: Int,
    isCharging: Boolean,
    isWifiOn: Boolean,
    isBluetoothOn: Boolean,
    alignment: TaskbarAlignment,
    isAutohide: Boolean,
    isDrawerOpen: Boolean,
    onEverythingButtonClick: () -> Unit,
    onAppClick: (AppMetaData) -> Unit,
    onWindowIconClick: (String) -> Unit,
    onSystemTrayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(TaskbarBg)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(ChromeGlassBorder, Color.Transparent)
                    ),
                    shape = androidx.compose.ui.graphics.RectangleShape
                )
                .padding(horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Everything Button (Left)
                EverythingLauncherButton(
                    isActive = isDrawerOpen,
                    onClick = onEverythingButtonClick
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Center Apps Shelf Alignment
                val appsArrangement = when (alignment) {
                    TaskbarAlignment.LEFT -> Arrangement.Start
                    TaskbarAlignment.CENTER -> Arrangement.Center
                    TaskbarAlignment.RIGHT -> Arrangement.End
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalArrangement = appsArrangement,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Combine pinned apps and running app windows
                    val pinnedIds = pinnedApps.map { it.id }.toSet()
                    val runningWindows = openWindows.filterNot { it.id in pinnedIds }

                    // Pinned Apps
                    pinnedApps.forEach { app ->
                        val isOpen = openWindows.any { it.id == app.id }
                        val isActive = activeWindowId == app.id
                        val window = openWindows.find { it.id == app.id }

                        ShelfAppIconItem(
                            appTitle = app.title,
                            icon = app.icon,
                            isOpen = isOpen,
                            isActive = isActive,
                            onClick = {
                                if (isOpen && window != null) {
                                    onWindowIconClick(window.id)
                                } else {
                                    onAppClick(app)
                                }
                            }
                        )
                    }

                    // Extra Running Apps (not pinned)
                    runningWindows.forEach { win ->
                        val isActive = activeWindowId == win.id
                        ShelfAppIconItem(
                            appTitle = win.title,
                            icon = win.icon,
                            isOpen = true,
                            isActive = isActive,
                            onClick = { onWindowIconClick(win.id) }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // System Tray (Right)
                SystemTrayWidget(
                    clockTime = clockTime,
                    batteryLevel = batteryLevel,
                    isCharging = isCharging,
                    isWifiOn = isWifiOn,
                    isBluetoothOn = isBluetoothOn,
                    onClick = onSystemTrayClick
                )
            }
        }
    }
}

@Composable
fun EverythingLauncherButton(
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(if (isActive) NeonCyan.copy(alpha = 0.25f) else Color(0xFF28233D))
            .border(
                width = 1.5.dp,
                color = if (isActive) NeonCyan else ChromeGlassBorder,
                shape = CircleShape
            )
            .clickable { onClick() }
            .testTag("everything_launcher_button"),
        contentAlignment = Alignment.Center
    ) {
        // Inner white circle with glowing dots
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

@Composable
fun ShelfAppIconItem(
    appTitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isOpen: Boolean,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxHeight()
            .width(44.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .testTag("shelf_app_${appTitle.lowercase().replace(" ", "_")}")
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isActive) Color(0xFF3B335A)
                    else if (isOpen) Color(0xFF2A2445)
                    else Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = appTitle,
                tint = if (isActive) NeonCyan else TextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Active glowing indicator line underneath open running windows
        if (isOpen) {
            Box(
                modifier = Modifier
                    .width(if (isActive) 18.dp else 8.dp)
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(if (isActive) NeonCyan else NeonMagenta.copy(alpha = 0.8f))
            )
        } else {
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
fun SystemTrayWidget(
    clockTime: String,
    batteryLevel: Int,
    isCharging: Boolean,
    isWifiOn: Boolean,
    isBluetoothOn: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF28233D))
            .border(1.dp, ChromeGlassBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("system_tray_widget")
    ) {
        // Wi-Fi Icon
        Icon(
            imageVector = if (isWifiOn) Icons.Default.Wifi else Icons.Default.WifiOff,
            contentDescription = "Wi-Fi",
            tint = if (isWifiOn) NeonCyan else Color.Gray,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Bluetooth Icon
        Icon(
            imageVector = if (isBluetoothOn) Icons.Default.Bluetooth else Icons.Default.BluetoothDisabled,
            contentDescription = "Bluetooth",
            tint = if (isBluetoothOn) NeonCyan else Color.Gray,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Battery level & icon
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull,
                contentDescription = "Battery",
                tint = if (batteryLevel > 20) NeonCyan else Color.Red,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$batteryLevel%",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Vertical divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(14.dp)
                .background(Color(0xFF4A4268))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Clock Time
        Text(
            text = clockTime,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        )
    }
}
