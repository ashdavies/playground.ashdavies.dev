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
import androidx.compose.ui.graphics.ColorFilter
import com.slack.circuit.Screen
import io.ashdavies.playground.home.HomeScreen

@Composable
internal fun EventsBottomBar(
    selected: Screen,
    modifier: Modifier = Modifier,
    onClick: (Screen) -> Unit = { },
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        modifier = modifier,
    ) {
        NavigationBarItem(
            onClick = { onClick(HomeScreen) },
            selected = selected is HomeScreen,
            icon = {
                Image(
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                )
            },
        )

        NavigationBarItem(
            onClick = { onClick(EventsScreen.Profile) },
            selected = selected is EventsScreen.Profile,
            icon = {
                Image(
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                )
            },
        )
    }
}
