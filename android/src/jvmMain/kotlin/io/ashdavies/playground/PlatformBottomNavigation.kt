package io.ashdavies.playground

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
public actual fun PlatformBottomNavigation(
    backgroundColor: Color,
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit
) = BottomNavigation(content = content)
