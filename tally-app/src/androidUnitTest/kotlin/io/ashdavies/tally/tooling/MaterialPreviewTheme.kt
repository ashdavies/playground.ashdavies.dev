package io.ashdavies.tally.tooling

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.ashdavies.tally.material.LocalWindowSizeClass
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.PreviewContextConfigurationEffect

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun MaterialPreviewTheme(
    size: DpSize = DpSize(1280.dp, 720.dp),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        PreviewContextConfigurationEffect()

        MaterialTheme(if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
            @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
            CompositionLocalProvider(LocalWindowSizeClass provides WindowSizeClass.calculateFromSize(size)) {
                Surface(content = content)
            }
        }
    }
}
