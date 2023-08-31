package io.ashdavies.playground.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.playground.activity.ActivityScreen

@Parcelize
public object HomeScreen : Screen {
    public sealed interface Event : CircuitUiEvent {
        public data class ChildNav(val navEvent: NavEvent) : Event
        public data class BottomNav(val screen: Screen) : Event
    }

    public data class State(
        val screen: Screen,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

@Composable
internal fun HomePresenter(navigator: Navigator): HomeScreen.State {
    var screen by remember { mutableStateOf<Screen>(ActivityScreen) }

    return HomeScreen.State(screen) { event ->
        when (event) {
            is HomeScreen.Event.ChildNav -> navigator.onNavEvent(event.navEvent)
            is HomeScreen.Event.BottomNav -> screen = event.screen
        }
    }
}
