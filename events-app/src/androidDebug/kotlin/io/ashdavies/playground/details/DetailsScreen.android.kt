package io.ashdavies.playground.details

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.ashdavies.playground.DroidconBerlin
import io.ashdavies.playground.Event

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun DetailsScreen(item: Event = DroidconBerlin) {
    DetailsScreen(DetailsScreen.State(item) { })
}
