package io.ashdavies.party.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.identity.IdentityManager
import io.ashdavies.identity.IdentityState
import io.ashdavies.party.config.booleanConfigAsState
import io.ashdavies.party.config.showPastEvents
import io.ashdavies.party.past.GalleryScreen
import io.ashdavies.party.upcoming.UpcomingEventsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun HomePresenter(
    identityManager: IdentityManager,
    coroutineScope: CoroutineScope,
    navigator: Navigator,
): HomeScreen.State {
    val identityState by identityManager.state.collectAsState(IdentityState.Unauthenticated)
    val isHomeEnabled by booleanConfigAsState { showPastEvents() }

    val initialScreen = if (isHomeEnabled) GalleryScreen else UpcomingEventsScreen
    var screen by rememberRetained { mutableStateOf<Screen>(initialScreen) }

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
