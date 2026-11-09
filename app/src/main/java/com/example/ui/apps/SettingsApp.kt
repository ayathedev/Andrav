package com.example.ui.apps

import android.content.Context
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignHorizontalCenter
import androidx.compose.material.icons.filled.AlignHorizontalLeft
import androidx.compose.material.icons.filled.AlignHorizontalRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.ViewSidebar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TaskbarAlignment
import com.example.model.WallpaperStyle
import com.example.ui.theme.ChromeGlassBorder
import com.example.ui.theme.ChromeSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SettingsApp(
    wallpaperStyle: WallpaperStyle,
    taskbarAlignment: TaskbarAlignment,
    isTaskbarAutohide: Boolean,
    isDefaultLauncher: Boolean,
    onWallpaperChange: (WallpaperStyle) -> Unit,
    onTaskbarAlignmentChange: (TaskbarAlignment) -> Unit,
    onTaskbarAutohideToggle: () -> Unit,
    onLaunchHomeSettings: (Context) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ChromeSurface)
            .verticalScroll(scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // "Set as Default Home App" Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isDefaultLauncher) Color(0xFF1B2E28) else Color(0xFF2E1B28)
                )
                .border(
                    width = 1.5.dp,
                    color = if (isDefaultLauncher) NeonEmerald else NeonMagenta,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
                .testTag("default_launcher_card")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isDefaultLauncher) Icons.Default.CheckCircle else Icons.Default.Home,
                    contentDescription = null,
                    tint = if (isDefaultLauncher) NeonEmerald else NeonMagenta,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isDefaultLauncher) "Aya OS is your Default Home App" else "Set Aya OS as Default Home Launcher",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = if (isDefaultLauncher) "Full tablet desktop experience enabled" else "Tap below to launch Android Home Settings and choose Aya OS",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }

                if (!isDefaultLauncher) {
                    Button(
                        onClick = { onLaunchHomeSettings(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_set_default_launcher")
                    ) {
                        Text("Set Default", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Wallpaper Gallery Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E1933))
                .border(1.dp, ChromeGlassBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Palette, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Wallpaper & Canvas Style",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 15.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WallpaperStyle.entries.forEach { style ->
                    val isSelected = style == wallpaperStyle
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when (style) {
                                    WallpaperStyle.FLUID_DARK_WAVES -> Color(0xFF1F1A3A)
                                    WallpaperStyle.CYBER_GRID -> Color(0xFF0D0A1A)
                                    WallpaperStyle.NEON_AURORA -> Color(0xFF2A163B)
                                    WallpaperStyle.OBSIDIAN_NEBULA -> Color(0xFF120D24)
                                }
                            )
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) NeonCyan else ChromeGlassBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onWallpaperChange(style) }
                            .padding(8.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Text(
                            text = style.displayName,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isSelected) NeonCyan else TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // Shelf / Taskbar Configuration Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E1933))
                .border(1.dp, ChromeGlassBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ViewSidebar, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Shelf & Taskbar Settings",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 15.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Alignment buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val alignOptions = listOf(
                    TaskbarAlignment.LEFT to Icons.Default.AlignHorizontalLeft,
                    TaskbarAlignment.CENTER to Icons.Default.AlignHorizontalCenter,
                    TaskbarAlignment.RIGHT to Icons.Default.AlignHorizontalRight
                )

                alignOptions.forEach { (align, icon) ->
                    val isSelected = align == taskbarAlignment
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFF2E264D) else Color(0xFF141024))
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NeonCyan else ChromeGlassBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onTaskbarAlignmentChange(align) }
                            .padding(vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = align.name,
                            tint = if (isSelected) NeonCyan else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = align.name,
                            color = if (isSelected) TextPrimary else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Autohide Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Auto-hide Taskbar Shelf", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Text("Hide taskbar when not hovered", color = TextSecondary, fontSize = 11.sp)
                }
                Switch(
                    checked = isTaskbarAutohide,
                    onCheckedChange = { onTaskbarAutohideToggle() },
                    colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                )
            }
        }

        // System Hardware Diagnostics Pane
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E1933))
                .border(1.dp, ChromeGlassBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Memory, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hardware & Device Diagnostics",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 15.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DiagnosticItem("RAM Usage", "4.1 GB / 8.0 GB (51%)", 0.51f, NeonCyan)
                DiagnosticItem("Storage Usage", "45.2 GB / 128 GB (35%)", 0.35f, NeonEmerald)
                DiagnosticItem("CPU Load", "18% Load (8 Cores Active)", 0.18f, NeonMagenta)
                DiagnosticItem("Battery Status", "92% (Discharging)", 0.92f, NeonCyan)
            }
        }
    }
}

@Composable
fun DiagnosticItem(
    title: String,
    detail: String,
    progress: Float,
    barColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(detail, color = TextSecondary, fontSize = 11.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = barColor,
 trackColor = Color(0xFF2B2544)
        )
    }
}
