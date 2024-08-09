package io.ashdavies.party

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
import io.ashdavies.events.EventsScreen
import io.ashdavies.gallery.GalleryScreen
import io.ashdavies.identity.CredentialQueries
import io.ashdavies.identity.IdentityManager
import io.ashdavies.identity.IdentityState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun AfterPartyPresenter(
    platformContext: PlatformContext,
    navigator: Navigator,
): AfterPartyScreen.State = AfterPartyPresenter(
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
private fun AfterPartyPresenter(
    identityManager: IdentityManager,
    coroutineScope: CoroutineScope,
    navigator: Navigator,
): AfterPartyScreen.State {
    val identityState by identityManager.state.collectAsState(IdentityState.Unauthenticated)
    val isHomeEnabled by booleanConfigAsState { isHomeEnabled() }

    val initialScreen = if (isHomeEnabled) GalleryScreen else EventsScreen
    var screen by rememberRetained { mutableStateOf<Screen>(initialScreen) }

    return AfterPartyScreen.State(
        identityState = identityState,
        screen = screen,
    ) { event ->
        when (event) {
            is AfterPartyScreen.Event.Login -> coroutineScope.launch { identityManager.signIn() }
            is AfterPartyScreen.Event.ChildNav -> navigator.onNavEvent(event.navEvent)
            is AfterPartyScreen.Event.BottomNav -> screen = event.screen
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
