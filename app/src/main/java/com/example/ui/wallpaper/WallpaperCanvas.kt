package com.example.ui.wallpaper

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import com.example.model.WallpaperStyle
import com.example.ui.theme.ChromeBackground
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import kotlin.math.sin

@Composable
fun WallpaperCanvas(
    style: WallpaperStyle,
    onLongPress: (Offset) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wallpaper_wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ChromeBackground)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { offset -> onLongPress(offset) }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            when (style) {
                WallpaperStyle.FLUID_DARK_WAVES -> {
                    // Deep dark gradient base
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF1F1A3A),
                                Color(0xFF130F26),
                                ChromeBackground
                            ),
                            center = Offset(width * 0.5f, height * 0.4f),
                            radius = width * 0.7f
                        )
                    )

                    // Draw Wave 1 (Neon Cyan)
                    val path1 = Path()
                    path1.moveTo(0f, height * 0.65f)
                    for (x in 0..width.toInt() step 20) {
                        val y = height * 0.65f + sin((x / width * 4 * Math.PI + waveOffset).toDouble()).toFloat() * 45f
                        path1.lineTo(x.toFloat(), y)
                    }
                    path1.lineTo(width, height)
                    path1.lineTo(0f, height)
                    path1.close()

                    drawPath(
                        path = path1,
                        brush = Brush.verticalGradient(
                            colors = listOf(NeonCyan.copy(alpha = 0.25f), Color.Transparent)
                        )
                    )
                    drawPath(
                        path = path1,
                        color = NeonCyan.copy(alpha = 0.7f * glowAlpha),
                        style = Stroke(width = 3f)
                    )

                    // Draw Wave 2 (Neon Magenta)
                    val path2 = Path()
                    path2.moveTo(0f, height * 0.75f)
                    for (x in 0..width.toInt() step 20) {
                        val y = height * 0.72f + sin((x / width * 3 * Math.PI - waveOffset + 1).toDouble()).toFloat() * 55f
                        path2.lineTo(x.toFloat(), y)
                    }
                    path2.lineTo(width, height)
                    path2.lineTo(0f, height)
                    path2.close()

                    drawPath(
                        path = path2,
                        brush = Brush.verticalGradient(
                            colors = listOf(NeonMagenta.copy(alpha = 0.20f), Color.Transparent)
                        )
                    )
                    drawPath(
                        path = path2,
                        color = NeonMagenta.copy(alpha = 0.65f * glowAlpha),
                        style = Stroke(width = 3.5f)
                    )

                    // Draw Wave 3 (Deep Neon Purple)
                    val path3 = Path()
                    path3.moveTo(0f, height * 0.55f)
                    for (x in 0..width.toInt() step 25) {
                        val y = height * 0.52f + sin((x / width * 2 * Math.PI + waveOffset * 0.5).toDouble()).toFloat() * 35f
                        path3.lineTo(x.toFloat(), y)
                    }
                    drawPath(
                        path = path3,
                        color = NeonPurple.copy(alpha = 0.5f),
                        style = Stroke(width = 2f)
                    )
                }

                WallpaperStyle.CYBER_GRID -> {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D0A1A), Color(0xFF1E1738))
                        )
                    )

                    // Grid lines
                    val step = 60f
                    var x = 0f
                    while (x < width) {
                        drawLine(
                            color = NeonCyan.copy(alpha = 0.12f),
                            start = Offset(x, 0f),
                            end = Offset(x, height),
                            strokeWidth = 1f
                        )
                        x += step
                    }
                    var y = 0f
                    while (y < height) {
                        drawLine(
                            color = NeonMagenta.copy(alpha = 0.12f),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1f
                        )
                        y += step
                    }
                }

                WallpaperStyle.NEON_AURORA -> {
                    drawRect(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFF0F0C1B),
                                NeonPurple.copy(alpha = 0.3f),
                                NeonCyan.copy(alpha = 0.25f),
                                NeonMagenta.copy(alpha = 0.3f),
                                Color(0xFF0F0C1B)
                            ),
                            center = Offset(width * 0.5f, height * 0.3f)
                        )
                    )
                }

                WallpaperStyle.OBSIDIAN_NEBULA -> {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF281E46),
                                Color(0xFF140E26),
                                Color(0xFF080611)
                            ),
                            center = Offset(width * 0.7f, height * 0.3f),
                            radius = width * 0.9f
                        )
                    )
                }
            }

            // Draw "AYA OS TABLET EDITION" Watermark Typography into Canvas Center
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = (width * 0.038f).coerceIn(24f, 44f)
                isFakeBoldText = true
                letterSpacing = 0.35f
                alpha = (60 * glowAlpha).toInt()
                textAlign = android.graphics.Paint.Align.CENTER
                setShadowLayer(18f, 0f, 0f, android.graphics.Color.parseColor("#00F2FE"))
            }

            drawContext.canvas.nativeCanvas.drawText(
                "AYA OS TABLET EDITION",
                width / 2f,
                height * 0.42f,
                paint
            )

            // Sub-tagline
            val subPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#94A3B8")
                textSize = (width * 0.016f).coerceIn(12f, 20f)
                letterSpacing = 0.25f
                alpha = 110
                textAlign = android.graphics.Paint.Align.CENTER
            }

            drawContext.canvas.nativeCanvas.drawText(
                "POWERED BY CHROME OS ARCHITECTURE",
                width / 2f,
                height * 0.48f,
                subPaint
            )
        }
    }
}
