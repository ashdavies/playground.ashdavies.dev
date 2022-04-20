package io.ashdavies.playground.platform

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable

@Composable
internal actual fun PlatformScaffold(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) = Scaffold(
    bottomBar = bottomBar,
    content = content,
    topBar = topBar,
)
