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
            icon = { Image(Icons.Default.Home, null) },
            onClick = { onClick(EventsScreen.Home) },
            selected = selected is EventsScreen.Home,
        )

        NavigationBarItem(
            icon = { Image(Icons.Default.Person, null) },
            onClick = { onClick(EventsScreen.Profile) },
            selected = selected is EventsScreen.Profile,
        )
    }
}
