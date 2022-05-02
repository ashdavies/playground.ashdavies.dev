package io.ashdavies.playground.platform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.ashdavies.playground.EventsRoot

@Composable
public expect fun PlatformBottomNavigation(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable RowScope.() -> Unit
)

@Composable
public expect fun RowScope.PlatformBottomNavigationItem(
    selected: Boolean = false,
    onClick: () -> Unit = { },
    icon: @Composable () -> Unit = { },
)

@Composable
internal fun PlaygroundBottomBar(child: EventsRoot.Child, modifier: Modifier = Modifier) {
    PlatformBottomNavigation(modifier, MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)) {
        PlatformBottomNavigationItem(
            icon = { Image(Icons.Default.Home, null) },
            onClick = { child.navigation.navigateToEvents() },
            selected = child is EventsRoot.Child.Events,
        )

        PlatformBottomNavigationItem(
            icon = { Image(Icons.Default.Person, null) },
            onClick = { child.navigation.navigateToProfile() },
            selected = child is EventsRoot.Child.Profile,
        )
    }
}
