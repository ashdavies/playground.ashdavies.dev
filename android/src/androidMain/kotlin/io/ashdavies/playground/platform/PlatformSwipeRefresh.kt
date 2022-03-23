package io.ashdavies.playground.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
public actual fun PlatformSwipeRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit,
) = SwipeRefresh(
    state = rememberSwipeRefreshState(isRefreshing),
    indicatorPadding = LocalScaffoldPadding.current,
    onRefresh = onRefresh,
    modifier = modifier,
    content = content,
)
