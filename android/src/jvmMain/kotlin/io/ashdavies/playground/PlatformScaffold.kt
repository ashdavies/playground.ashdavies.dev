package io.ashdavies.playground

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable

@Composable
actual fun PlatformScaffold(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) = Scaffold(
    bottomBar = bottomBar,
    content = content,
    topBar = topBar,
)
