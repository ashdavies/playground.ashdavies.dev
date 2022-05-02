@file:JvmName("TopAppBarAndroid")

package io.ashdavies.playground

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues

@Composable
public actual fun rememberInsetsPaddingValues(
    applyStart: Boolean,
    applyTop: Boolean,
    applyEnd: Boolean,
    applyBottom: Boolean,
): PaddingValues = rememberInsetsPaddingValues(
    insets = LocalWindowInsets.current.statusBars,
    applyStart = applyStart,
    applyTop = applyTop,
    applyEnd = applyEnd,
    applyBottom = applyBottom,
)

@Composable
public actual fun LargeTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?
) {
    LargeTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}
