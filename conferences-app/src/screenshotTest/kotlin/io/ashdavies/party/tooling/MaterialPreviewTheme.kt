package io.ashdavies.party.tooling

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.ashdavies.party.material.LocalWindowSizeClass

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal fun MaterialPreviewTheme(
    size: DpSize = DpSize(1280.dp, 720.dp),
    content: @Composable () -> Unit,
) {
    MaterialTheme(if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
        CompositionLocalProvider(
            value = LocalWindowSizeClass provides WindowSizeClass.calculateFromSize(size),
            //content = { Surface(content = content) },
            content = content,
        )
    }
}
