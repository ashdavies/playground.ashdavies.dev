package dev.ashdavies.playground.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.playground.ui.CircularWavyProgressIndicator
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BottomBarScaffoldReady(
    state: BottomBarScaffoldScreen.State.Ready,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomAppBar(Modifier.visible(state.items.isNotEmpty())) {
                NavigationBar {
                    state.items.forEach {
                        NavigationBarItem(
                            selected = it.selected,
                            onClick = { state.eventSink(it.screen) },
                            icon = { NavigationBarImage(it.icon) },
                            label = { Text(stringResource(it.label)) },
                        )
                    }
                }
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(
            insets = WindowInsets.systemBars.only(WindowInsetsSides.Vertical),
        ),
    ) { contentPadding ->
        CircuitContent(
            screen = state.selectedScreen,
            modifier = Modifier.padding(contentPadding),
            onNavEvent = { state.eventSink(BottomBarScaffoldScreen.Event.ChildNav(it)) },
            unavailableContent = { screen, modifier ->
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Color.Blue)
                        .padding(32.dp),
                ) {
                    Text("$screen")
                }
            },
        )
    }
}

@Preview
@Composable
internal fun BottomBarScaffoldLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularWavyProgressIndicator()
    }
}

@Composable
private fun NavigationBarImage(imageVector: ImageVector) {
    Image(
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        contentDescription = null,
        imageVector = imageVector,
    )
}

private fun BottomBarScaffoldScreen.State.Ready.eventSink(value: Screen) {
    eventSink(BottomBarScaffoldScreen.Event.BottomNav(value))
}
