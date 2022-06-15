package io.ashdavies.playground.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.ashdavies.playground.windowInsetsPadding

@Composable
public actual fun PlatformSwipeRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit,
) = SwipeRefresh(
    state = rememberSwipeRefreshState(isRefreshing),
    modifier = modifier.windowInsetsPadding(),
    onRefresh = onRefresh,
    content = content,
)
