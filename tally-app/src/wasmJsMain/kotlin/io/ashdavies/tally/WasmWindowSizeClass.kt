package io.ashdavies.tally

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

internal data class WasmWindowSize(
    val widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    val heightSizeClass: WindowHeightSizeClass = WindowHeightSizeClass.Compact,
)
