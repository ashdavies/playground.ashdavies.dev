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

@Composable
internal fun EventsBottomBar(
    state: EventsState,
    modifier: Modifier = Modifier,
) {
    val sink = state.sink

    EventsBottomBar(
        onClick = { sink(EventsEvent.BottomNav(it)) },
        selected = state.current,
        modifier = modifier,
    )
}

@Composable
internal fun EventsBottomBar(
    selected: EventsScreen,
    modifier: Modifier = Modifier,
    onClick: (EventsScreen) -> Unit = { },
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        modifier = modifier,
    ) {
        NavigationBarItem(
            onClick = { onClick(EventsScreen.Home) },
            selected = selected is EventsScreen.Home,
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
