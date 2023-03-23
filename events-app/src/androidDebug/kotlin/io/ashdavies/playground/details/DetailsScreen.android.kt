package io.ashdavies.playground.details

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.ashdavies.playground.DroidconBerlin
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsScreen
import io.ashdavies.playground.EventsState

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DetailsScreen(item: Event = DroidconBerlin) {
    DetailsScreen(EventsState(EventsScreen.Details(item.id)) { })
}
