package io.ashdavies.playground.platform

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

@Composable
expect fun PlatformScaffold(
    topBar: @Composable () -> Unit = { },
    bottomBar: @Composable () -> Unit = { },
    content: @Composable (PaddingValues) -> Unit
)
