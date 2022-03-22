@file:JvmName("PlatformTopAppBarAndroid")

package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar

@Composable
actual fun PlatformTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)?,
    modifier: Modifier,
) = TopAppBar(
    title = title,
    contentPadding = rememberInsetsPaddingValues(
        LocalWindowInsets.current.statusBars,
        applyBottom = false,
    ),
    navigationIcon = navigationIcon,
    modifier = modifier,
)
