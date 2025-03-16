package io.ashdavies.tally.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.identity.IdentityManager
import io.ashdavies.identity.IdentityState
import io.ashdavies.tally.upcoming.UpcomingEventsScreen
import kotlinx.coroutines.launch

@Composable
internal fun HomePresenter(
    identityManager: IdentityManager,
    navigator: Navigator,
): HomeScreen.State {
    val identityState by identityManager.state.collectAsState(IdentityState.Unauthenticated)
    var screen by rememberRetained { mutableStateOf<Screen>(UpcomingEventsScreen) }
    val coroutineScope = rememberCoroutineScope()

    return HomeScreen.State(
        identityState = identityState,
        screen = screen,
    ) { event ->
        when (event) {
            is HomeScreen.Event.Login -> coroutineScope.launch { identityManager.signIn() }
            is HomeScreen.Event.ChildNav -> navigator.onNavEvent(event.navEvent)
            is HomeScreen.Event.BottomNav -> screen = event.screen
        }
    }
}
