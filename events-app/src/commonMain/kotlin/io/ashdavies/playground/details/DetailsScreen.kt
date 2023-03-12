package io.ashdavies.playground.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.ashdavies.playground.EventsScreen
import io.ashdavies.playground.EventsState

@Composable
internal fun DetailsScreen(state: EventsState, modifier: Modifier = Modifier) {
    check(state.current is EventsScreen.Details)
    Text(state.current.event.name, modifier)
}
