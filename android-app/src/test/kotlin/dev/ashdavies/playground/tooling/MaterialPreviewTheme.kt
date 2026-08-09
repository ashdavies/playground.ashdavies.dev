package dev.ashdavies.playground.tooling

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.PreviewContextConfigurationEffect

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun MaterialPreviewTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        PreviewContextConfigurationEffect()

        MaterialExpressiveTheme(if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
            Surface(content = content)
        }
    }
}
