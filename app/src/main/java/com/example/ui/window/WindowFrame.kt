package com.example.ui.window

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.West
import androidx.compose.material.icons.filled.East
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.DesktopWindow
import com.example.ui.theme.ChromeGlassBorder
import com.example.ui.theme.ChromeSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.WindowHeaderActive
import com.example.ui.theme.WindowHeaderBg
import kotlin.math.roundToInt

@Composable
fun WindowFrame(
    window: DesktopWindow,
    isActive: Boolean,
    onFocus: () -> Unit,
    onDrag: (Offset) -> Unit,
    onResize: (Offset) -> Unit,
    onMinimize: () -> Unit,
    onMaximizeToggle: () -> Unit,
    onSnapLeft: () -> Unit,
    onSnapRight: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (window.isMinimized) return

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = (configuration.screenHeightDp - 52).dp // minus shelf height

    val finalWidth = when {
        window.isMaximized -> screenWidth
        window.isSnappedLeft || window.isSnappedRight -> screenWidth / 2
        else -> window.size.width
    }

    val finalHeight = when {
        window.isMaximized || window.isSnappedLeft || window.isSnappedRight -> screenHeight
        else -> window.size.height
    }

    val finalOffsetX = when {
        window.isMaximized || window.isSnappedLeft -> 0f
        window.isSnappedRight -> (screenWidth.value - (screenWidth.value / 2)) * configuration.densityDpi / 160f
        else -> window.offset.x
    }

    val finalOffsetY = when {
        window.isMaximized || window.isSnappedLeft || window.isSnappedRight -> 0f
        else -> window.offset.y
    }

    Box(
        modifier = modifier
            .offset { IntOffset(finalOffsetX.roundToInt(), finalOffsetY.roundToInt()) }
            .size(finalWidth, finalHeight)
            .zIndex(window.zIndex)
            .shadow(
                elevation = if (isActive) 18.dp else 6.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = if (isActive) NeonCyan else Color.Black
            )
            .clip(RoundedCornerShape(16.dp))
            .background(ChromeSurface)
            .border(
                width = if (isActive) 2.dp else 1.dp,
                color = if (isActive) NeonCyan else ChromeGlassBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onFocus)
            .testTag("window_${window.id}")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ChromeOS Title Bar Controls Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(if (isActive) WindowHeaderActive else WindowHeaderBg)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            onDrag(dragAmount)
                        }
                    }
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = window.icon,
                    contentDescription = null,
                    tint = if (isActive) NeonCyan else Color.LightGray,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = window.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        fontSize = 13.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Controls: Snap Left, Snap Right, Minimize, Maximize, Close
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    // Snap Left Half
                    IconButton(
                        onClick = onSnapLeft,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.West,
                            contentDescription = "Snap Left",
                            tint = Color.LightGray,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Snap Right Half
                    IconButton(
                        onClick = onSnapRight,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.East,
                            contentDescription = "Snap Right",
                            tint = Color.LightGray,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Minimize
                    IconButton(
                        onClick = onMinimize,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Minimize,
                            contentDescription = "Minimize",
                            tint = Color.LightGray,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Maximize / Restore
                    IconButton(
                        onClick = onMaximizeToggle,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (window.isMaximized) Icons.Default.CropSquare else Icons.Default.OpenInFull,
                            contentDescription = "Maximize",
                            tint = Color.LightGray,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Close
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE53935))
                            .clickable { onClose() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Window",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Window Inner Content Body
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                content()
            }
        }

        // Bottom-Right Corner Resize Handler
        if (!window.isMaximized && !window.isSnappedLeft && !window.isSnappedRight) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(20.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            onResize(dragAmount)
                        }
                    }
                    .padding(4.dp)
                    .testTag("window_resize_handle_${window.id}")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isActive) NeonCyan.copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.3f))
                )
            }
        }
    }
}
