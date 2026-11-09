package com.example.ui.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignHorizontalCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TaskbarAlignment
import com.example.ui.theme.ChromeGlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import kotlin.math.roundToInt

@Composable
fun DesktopContextMenu(
    isOpen: Boolean,
    offset: Offset,
    onDismiss: () -> Unit,
    onChangeWallpaperClick: () -> Unit,
    onNewNoteClick: () -> Unit,
    onOpenTerminalClick: () -> Unit,
    onSetDefaultLauncherClick: () -> Unit,
    onSetShelfAlignment: (TaskbarAlignment) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    Box(modifier = modifier.fillMaxSize()) {
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = onDismiss,
            modifier = Modifier
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .background(Color(0xFF1E1B2E))
                .border(1.dp, ChromeGlassBorder, RoundedCornerShape(14.dp))
                .padding(vertical = 4.dp)
                .testTag("desktop_context_menu")
        ) {
            DropdownMenuItem(
                text = {
                    ContextMenuItem(icon = Icons.Default.Palette, label = "Change Wallpaper")
                },
                onClick = {
                    onDismiss()
                    onChangeWallpaperClick()
                }
            )

            DropdownMenuItem(
                text = {
                    ContextMenuItem(icon = Icons.Default.Notes, label = "New Sticky Note")
                },
                onClick = {
                    onDismiss()
                    onNewNoteClick()
                }
            )

            DropdownMenuItem(
                text = {
                    ContextMenuItem(icon = Icons.Default.Terminal, label = "Open Terminal")
                },
                onClick = {
                    onDismiss()
                    onOpenTerminalClick()
                }
            )

            HorizontalDivider(color = ChromeGlassBorder, modifier = Modifier.padding(vertical = 4.dp))

            DropdownMenuItem(
                text = {
                    ContextMenuItem(icon = Icons.Default.Home, label = "Set Default Launcher")
                },
                onClick = {
                    onDismiss()
                    onSetDefaultLauncherClick()
                }
            )

            DropdownMenuItem(
                text = {
                    ContextMenuItem(icon = Icons.Default.AlignHorizontalCenter, label = "Shelf Alignment (Center)")
                },
                onClick = {
                    onDismiss()
                    onSetShelfAlignment(TaskbarAlignment.CENTER)
                }
            )
        }
    }
}

@Composable
fun ContextMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = NeonCyan,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            color = TextPrimary,
            fontSize = 13.sp
        )
    }
}
