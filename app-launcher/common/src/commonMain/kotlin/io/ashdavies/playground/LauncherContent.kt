package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
public fun LauncherContent(content: @Composable () -> Unit) {
    MaterialTheme(dynamicColorScheme()) {
        content()
    }
}
