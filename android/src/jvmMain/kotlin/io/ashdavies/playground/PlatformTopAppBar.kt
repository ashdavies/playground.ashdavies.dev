@file:JvmName("PlatformTopAppBarJvm")

package io.ashdavies.playground

import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)?,
    modifier: Modifier,
) = TopAppBar(
    navigationIcon = navigationIcon,
    modifier = modifier,
    title = title,
)
