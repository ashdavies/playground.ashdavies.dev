package dev.ashdavies.playground.tooling

import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import dev.ashdavies.playground.material.dynamicColorScheme

public class MaterialThemeWrapper : PreviewWrapperProvider {

    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        MaterialExpressiveTheme(dynamicColorScheme()) {
            Surface(content = content)
        }
    }
}
