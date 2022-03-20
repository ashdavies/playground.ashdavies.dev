package io.ashdavies.playground

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

@Composable
expect fun PlatformScaffold(
    topBar: @Composable () -> Unit = { },
    content: @Composable (PaddingValues) -> Unit
)
