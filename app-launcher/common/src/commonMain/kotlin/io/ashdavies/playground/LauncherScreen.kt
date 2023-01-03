package io.ashdavies.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import io.ashdavies.dominion.DominionRoot

@Composable
public fun LauncherScreen(componentContext: ComponentContext, state: LauncherState) {
    when (state.route) {
        LauncherRoute.Default -> LauncherScreen { state.sink(LauncherEvent.Click(it)) }
        LauncherRoute.Dominion -> DominionRoot(componentContext)
        LauncherRoute.Events -> EventsRoot(componentContext)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LauncherScreen(onClick: (LauncherRoute) -> Unit) {
    Scaffold(topBar = { LauncherTopAppBar() }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LauncherRow(Icons.Filled.Event, "Events") {
                onClick(LauncherRoute.Events)
            }

            LauncherRow(Icons.Filled.Grid4x4, "Dominion") {
                onClick(LauncherRoute.Dominion)
            }
        }
    }
}

@Composable
private fun LauncherTopAppBar() {
    SmallTopAppBar(
        title = { Text("Playground") },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Code,
                    contentDescription = null
                )
            }
        }
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
                modifier = Modifier.padding(16.dp)
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
