package io.ashdavies.playground.platform

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public actual fun PlatformSwipeRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        content = { content() },
        modifier = modifier,
    )
}
