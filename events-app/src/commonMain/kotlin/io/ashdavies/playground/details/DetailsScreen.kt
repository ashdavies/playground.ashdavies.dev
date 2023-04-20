package io.ashdavies.playground.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import io.ashdavies.playground.Event

@Composable
internal fun DetailsScreen(state: DetailsScreen.State, modifier: Modifier = Modifier) {
    val eventSink = state.eventSink

    if (state.event != null) {
        DetailsScreen(
            onBack = { eventSink(DetailsScreen.Event.NavEvent.Pop) },
            event = state.event,
            modifier = modifier,
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier.align(Alignment.Center))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DetailsScreen(event: Event, modifier: Modifier = Modifier, onBack: () -> Unit) {
    Scaffold(
        topBar = { DetailsTopAppBar(event.name, onBack) },
        modifier = modifier,
    ) { contentPadding ->
        Column(modifier = modifier.padding(contentPadding)) {
            Text(event.location)

            Row {
                Text(event.dateStart)
                Text(event.dateEnd)
            }

            Text(event.website)
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun DetailsTopAppBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        navigationIcon = { BackIconButton(onBack) },
        title = { Text(title) },
    )
}

@Composable
private fun BackIconButton(onClick: () -> Unit = { }) {
    IconButton(onClick) {
        Image(
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            painter = rememberVectorPainter(Icons.Filled.ArrowBack),
            contentDescription = null,
        )
    }
}
