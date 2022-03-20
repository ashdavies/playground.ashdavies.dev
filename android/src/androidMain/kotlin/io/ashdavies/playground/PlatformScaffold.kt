package io.ashdavies.playground

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ui.Scaffold

@Composable
actual fun PlatformScaffold(
    topBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) = Scaffold(
    content = content,
    topBar = topBar,
)
