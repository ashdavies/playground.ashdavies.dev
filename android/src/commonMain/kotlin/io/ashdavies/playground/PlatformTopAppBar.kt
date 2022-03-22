package io.ashdavies.playground

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public expect fun PlatformTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
)

@Composable
public fun PlatformTopAppBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) = PlatformTopAppBar(
    navigationIcon = navigationIcon,
    title = { Text(title) },
    modifier = modifier,
)
