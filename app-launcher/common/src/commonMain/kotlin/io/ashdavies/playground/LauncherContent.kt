package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import io.ashdavies.check.ProvideAppCheckToken

@Composable
public fun LauncherContent(content: @Composable () -> Unit) {
    ProvideAppCheckToken {
        MaterialTheme(dynamicColorScheme()) {
            content()
        }
    }
}
