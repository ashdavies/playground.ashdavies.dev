package io.ashdavies.tally.material

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalWindowSizeClass = staticCompositionLocalOf<WindowSizeClass> {
    error("WindowSizeClass not provided")
}

@Composable
internal fun ProvideLocalWindowSizeClass(
    windowSizeClass: WindowSizeClass,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalWindowSizeClass provides windowSizeClass,
        content = content,
    )
}
