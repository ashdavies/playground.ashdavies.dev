package io.ashdavies.playground.platform

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
import io.ashdavies.playground.EventsRoot

@Composable
public expect fun PlatformBottomNavigation(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
)

@Composable
internal fun PlaygroundBottomBar(child: EventsRoot.Child, modifier: Modifier = Modifier) {
    PlatformBottomNavigation(MaterialTheme.colors.surface.copy(alpha = 0.95f), modifier) {
        BottomNavigationItem(
            icon = { Image(Icons.Default.Home, null) },
            onClick = { child.navigation.navigateToEvents() },
            selected = child is EventsRoot.Child.Events,
        )

        BottomNavigationItem(
            icon = { Image(Icons.Default.Person, null) },
            onClick = { child.navigation.navigateToProfile() },
            selected = child is EventsRoot.Child.Profile,
        )
    }
}
