package io.ashdavies.gallery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun GalleryScreen(
    state: GalleryScreen.State,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { eventSink(GalleryScreen.Event.Capture) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        modifier = modifier,
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Hello World",
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
