package io.ashdavies.party.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.content.PlatformContext
import io.ashdavies.identity.CredentialQueries
import io.ashdavies.identity.IdentityManager
import io.ashdavies.identity.IdentityState
import io.ashdavies.party.config.booleanConfigAsState
import io.ashdavies.party.config.isHomeEnabled
import io.ashdavies.party.events.EventsScreen
import io.ashdavies.party.gallery.GalleryScreen
import io.ashdavies.party.sql.rememberLocalQueries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun HomePresenter(
    platformContext: PlatformContext,
    navigator: Navigator,
): HomeScreen.State = HomePresenter(
    identityManager = rememberIdentityManager(
        platformContext = platformContext,
        credentialQueries = rememberLocalQueries {
            it.credentialQueries
        },
    ),
    coroutineScope = rememberCoroutineScope(),
    navigator = navigator,
)

@Composable
private fun HomePresenter(
    identityManager: IdentityManager,
    coroutineScope: CoroutineScope,
    navigator: Navigator,
): HomeScreen.State {
    val identityState by identityManager.state.collectAsState(IdentityState.Unauthenticated)
    val isHomeEnabled by booleanConfigAsState { isHomeEnabled() }

    val initialScreen = if (isHomeEnabled) GalleryScreen else EventsScreen
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

@Composable
private fun rememberIdentityManager(
    platformContext: PlatformContext,
    credentialQueries: CredentialQueries,
): IdentityManager = remember(platformContext, credentialQueries) {
    IdentityManager(
        platformContext = platformContext,
        credentialQueries = credentialQueries,
    )
}
