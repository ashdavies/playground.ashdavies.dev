package dev.ashdavies.playground.tooling

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import dev.ashdavies.playground.material.dynamicColorScheme
import org.jetbrains.compose.resources.PreviewContextConfigurationEffect

@Composable
internal fun MaterialPreviewTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        PreviewContextConfigurationEffect()

        MaterialTheme(dynamicColorScheme()) {
            Surface(content = content)
        }
    }
}
