package dev.ashdavies.playground.ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.absoluteValue

public fun gradientBrush(seed: Long): Brush {
    val hue = (seed % 360).toFloat().absoluteValue

    return Brush.linearGradient(
        colors = listOf(
            Color.hsl(
                hue = hue,
                saturation = 0.5f,
                lightness = 0.45f,
            ),
            Color.hsl(
                hue = (hue + 60) % 360,
                saturation = 0.5f,
                lightness = 0.25f,
            ),
        ),
    )
}
