package io.ashdavies.playground.details

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import io.ashdavies.playground.EventsEvent
import io.ashdavies.playground.EventsScreen
import io.ashdavies.playground.EventsState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun DetailsScreen(state: EventsState, modifier: Modifier = Modifier) {
    check(state.current is EventsScreen.Details)
    val event = state.current.event

    Scaffold(
        topBar = { DetailsTopAppBar(event.name) { state.sink(EventsEvent.PopEvent) } },
        modifier = modifier,
    ) { contentPadding ->

    }
}

@Composable
@ExperimentalMaterial3Api
private fun DetailsTopAppBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        navigationIcon = { BackIconButton(onBack) },
        title = { Text(title) }
    )
}

@Composable
private fun BackIconButton(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Image(
            painter = rememberVectorPainter(Icons.Filled.ArrowBack),
            contentDescription = null,
        )
    }
}
