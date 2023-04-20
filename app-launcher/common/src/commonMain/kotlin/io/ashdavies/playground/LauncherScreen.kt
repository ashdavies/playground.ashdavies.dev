package io.ashdavies.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
public fun LauncherScreen(
    state: LauncherScreen.State,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    Scaffold(modifier, topBar = { LauncherTopAppBar() }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(state.entries) {
                    LauncherRow(it.image, it.text) {
                        eventSink(it.event)
                    }
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun LauncherTopAppBar() {
    TopAppBar(
        title = { Text("Playground") },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Code,
                    contentDescription = null,
                )
            }
        },
    )
}

@Composable
@ExperimentalMaterial3Api
private fun LauncherRow(
    image: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick() },
    ) {
        Card(modifier = modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(
                    modifier = Modifier.padding(end = 12.dp),
                    contentDescription = text,
                    imageVector = image,
                )

                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = text,
                )
            }
        }
    }
}
