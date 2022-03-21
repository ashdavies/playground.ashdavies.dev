package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public expect fun PlatformSwipeRefresh(
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = { },
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
)
