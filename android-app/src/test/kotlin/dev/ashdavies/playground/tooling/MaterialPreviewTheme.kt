package dev.ashdavies.playground.tooling

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import dev.ashdavies.playground.material.dynamicColorScheme
import org.jetbrains.compose.resources.PreviewContextConfigurationEffect

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal fun MaterialPreviewTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        PreviewContextConfigurationEffect()

        MaterialExpressiveTheme(dynamicColorScheme()) {
            Surface(content = content)
        }
    }
}
