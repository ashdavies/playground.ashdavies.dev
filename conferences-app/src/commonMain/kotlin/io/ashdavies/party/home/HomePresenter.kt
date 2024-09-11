package io.ashdavies.party.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.ashdavies.identity.IdentityManager
import io.ashdavies.identity.IdentityState
import io.ashdavies.party.config.booleanConfigAsState
import io.ashdavies.party.config.isHomeEnabled
import io.ashdavies.party.events.EventsScreen
import io.ashdavies.party.gallery.GalleryScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun HomePresenter(
    identityManager: IdentityManager,
    coroutineScope: CoroutineScope,
    onChildNav: (HomeScreen) -> Unit,
): HomeScreen.State {
    val identityState by identityManager.state.collectAsState(IdentityState.Unauthenticated)
    val isHomeEnabled by booleanConfigAsState { isHomeEnabled() }

    val initialScreen = if (isHomeEnabled) GalleryScreen else EventsScreen
    var screen by remember { mutableStateOf<HomeScreen>(initialScreen) }

    return HomeScreen.State(
        identityState = identityState,
        screen = screen,
    ) { event ->
        when (event) {
            is HomeScreen.Event.Login -> coroutineScope.launch { identityManager.signIn() }
            is HomeScreen.Event.ChildNav -> onChildNav(event.navEvent)
            is HomeScreen.Event.BottomNav -> screen = event.screen
        }
    }
}
