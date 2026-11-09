package com.example.ui.overlays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChromeGlassBorder
import com.example.ui.theme.ChromeGlassBackground
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun QuickSettingsTray(
    isOpen: Boolean,
    isWifiOn: Boolean,
    isBluetoothOn: Boolean,
    isNightLightOn: Boolean,
    isDndOn: Boolean,
    brightness: Float,
    volume: Float,
    isDefaultLauncher: Boolean,
    onWifiToggle: () -> Unit,
    onBluetoothToggle: () -> Unit,
    onNightLightToggle: () -> Unit,
    onDndToggle: () -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onOpenHomeSettings: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { onDismiss() },
        contentAlignment = Alignment.BottomEnd
    ) {
        AnimatedVisibility(
            visible = isOpen,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = 60.dp, end = 16.dp)
                    .width(360.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(ChromeGlassBackground)
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(NeonCyan.copy(alpha = 0.5f), ChromeGlassBorder)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable(enabled = false) { }
                    .padding(18.dp)
                    .testTag("quick_settings_tray")
            ) {
                // User Profile Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(NeonCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Aya Admin",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = "aya@chromeos.local",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF28233E))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = "Power",
                            tint = NeonMagenta,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 6 Quick Tiles
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.height(180.dp)
                ) {
                    item {
                        QuickTileItem(
                            title = if (isWifiOn) "AyaNet_5G" else "Wi-Fi Off",
                            subtitle = if (isWifiOn) "Connected" else "Disconnected",
                            icon = if (isWifiOn) Icons.Default.Wifi else Icons.Default.WifiOff,
                            isActive = isWifiOn,
                            onClick = onWifiToggle,
                            tag = "tile_wifi"
                        )
                    }
                    item {
                        QuickTileItem(
                            title = "Bluetooth",
                            subtitle = if (isBluetoothOn) "Active" else "Disabled",
                            icon = if (isBluetoothOn) Icons.Default.Bluetooth else Icons.Default.BluetoothDisabled,
                            isActive = isBluetoothOn,
                            onClick = onBluetoothToggle,
                            tag = "tile_bluetooth"
                        )
                    }
                    item {
                        QuickTileItem(
                            title = "Night Light",
                            subtitle = if (isNightLightOn) "On (Warm)" else "Off",
                            icon = Icons.Default.Nightlight,
                            isActive = isNightLightOn,
                            onClick = onNightLightToggle,
                            tag = "tile_nightlight"
                        )
                    }
                    item {
                        QuickTileItem(
                            title = "Do Not Disturb",
                            subtitle = if (isDndOn) "Silent Mode" else "Off",
                            icon = Icons.Default.DoNotDisturb,
                            isActive = isDndOn,
                            onClick = onDndToggle,
                            tag = "tile_dnd"
                        )
                    }
                    item {
                        QuickTileItem(
                            title = "Dark Theme",
                            subtitle = "Vibrant Glow",
                            icon = Icons.Default.Brightness6,
                            isActive = true,
                            onClick = { },
                            tag = "tile_theme"
                        )
                    }
                    item {
                        QuickTileItem(
                            title = "Launcher",
                            subtitle = if (isDefaultLauncher) "Default Home" else "Set Default",
                            icon = Icons.Default.Home,
                            isActive = isDefaultLauncher,
                            onClick = onOpenHomeSettings,
                            tag = "tile_launcher"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Brightness Slider
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Brightness6,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Display Brightness",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                        )
                    }
                    Slider(
                        value = brightness,
                        onValueChange = onBrightnessChange,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = Color(0xFF2B2544)
                        ),
                        modifier = Modifier.testTag("brightness_slider")
                    )
                }

                // Volume Slider
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Audio Volume",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                        )
                    }
                    Slider(
                        value = volume,
                        onValueChange = onVolumeChange,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = Color(0xFF2B2544)
                        ),
                        modifier = Modifier.testTag("volume_slider")
                    )
                }
            }
        }
    }
}

@Composable
fun QuickTileItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (isActive) Color(0xFF2E264D) else Color(0xFF201B35))
            .border(
                width = 1.dp,
                color = if (isActive) NeonCyan else ChromeGlassBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp)
            .testTag(tag)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isActive) NeonCyan else Color(0xFF332B50)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isActive) Color.Black else TextPrimary,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 11.sp
                ),
                maxLines = 1
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 9.sp
                ),
                maxLines = 1
            )
        }
    }
}
