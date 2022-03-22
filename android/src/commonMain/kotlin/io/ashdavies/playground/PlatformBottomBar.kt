package io.ashdavies.playground

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
public expect fun PlatformBottomNavigation(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
)

@Composable
internal fun PlaygroundBottomBar(
    child: PlaygroundRoot.Child,
    modifier: Modifier = Modifier,
) {
    PlatformBottomNavigation(MaterialTheme.colors.surface.copy(alpha = 0.95f)) {
        BottomNavigationItem(
            icon = { Image(Icons.Default.Home, null) },
            onClick = { child.navigation.navigateToEvents() },
            selected = child is PlaygroundRoot.Child.Events,
        )

        BottomNavigationItem(
            icon = { Image(Icons.Default.Person, null) },
            onClick = { child.navigation.navigateToProfile() },
            selected = child is PlaygroundRoot.Child.Profile,
        )
    }
}
