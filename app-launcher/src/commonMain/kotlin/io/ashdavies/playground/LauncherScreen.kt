package io.ashdavies.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext

@Composable
internal fun LauncherScreen(componentContext: ComponentContext) {
    var route by remember { mutableStateOf(LauncherRoute.Default) }

    when (route) {
        LauncherRoute.Default -> LauncherScreen { route = it }
        LauncherRoute.Dominion -> Unit
        LauncherRoute.Events -> Unit
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LauncherScreen(onClick: (LauncherRoute) -> Unit) {
    MaterialTheme {
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
}

@Composable
private fun LauncherTopAppBar() {
    SmallTopAppBar(title = { Text("Playground") })
}

@Composable
@ExperimentalMaterial3Api
private fun LauncherRow(image: ImageVector, text: String, onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Card(modifier = Modifier.padding(12.dp)) {
            Row {
                Icon(image, contentDescription = text)
                Text(text)
            }
        }
    }
}
