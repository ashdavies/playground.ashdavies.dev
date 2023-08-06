package io.ashdavies.playground.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.foundation.CircuitContent

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreen(state: HomeScreen.State, modifier: Modifier = Modifier) {
    val eventSink = state.eventSink

    Scaffold(
        bottomBar = { HomeBottomBar(state.screen) { eventSink(HomeScreen.Event.BottomNav(it)) } },
        modifier = modifier,
    ) {
        CircuitContent(
            onNavEvent = { eventSink(HomeScreen.Event.ChildNav(it)) },
            screen = state.screen,
        )
    }
}
