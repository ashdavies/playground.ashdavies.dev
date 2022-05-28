package io.ashdavies.playground

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun EventsBottomBar(child: EventsRoot.Child, modifier: Modifier = Modifier) {
    NavigationBar(modifier, MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)) {
        NavigationBarItem(
            icon = { Image(Icons.Default.Home, null) },
            onClick = { child.navigation.navigateToEvents() },
            selected = child is EventsRoot.Child.Events,
        )

        NavigationBarItem(
            icon = { Image(Icons.Default.Person, null) },
            onClick = { child.navigation.navigateToProfile() },
            selected = child is EventsRoot.Child.Profile,
        )
    }
}
