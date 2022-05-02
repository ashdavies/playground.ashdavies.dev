package io.ashdavies.playground.platform

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
public actual fun PlatformBottomNavigation(
    modifier: Modifier,
    backgroundColor: Color,
    content: @Composable RowScope.() -> Unit
) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = backgroundColor,
        content = content
    )
}

@Composable
public actual fun RowScope.PlatformBottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    BottomNavigationItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
    )
}
