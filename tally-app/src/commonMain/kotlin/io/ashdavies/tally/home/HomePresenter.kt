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
import io.ashdavies.config.RemoteConfig
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.isDebuggable
import io.ashdavies.identity.IdentityManager
import io.ashdavies.identity.IdentityState
import io.ashdavies.tally.config.booleanConfigAsState
import io.ashdavies.tally.config.isGalleryEnabled
import io.ashdavies.tally.config.isRoutesEnabled
import io.ashdavies.tally.upcoming.UpcomingScreen
import kotlinx.coroutines.launch

@Composable
internal fun HomePresenter(
    platformContext: PlatformContext,
    remoteConfig: RemoteConfig,
    identityManager: IdentityManager,
    navigator: Navigator,
): HomeScreen.State {
    var screen by rememberRetained { mutableStateOf<Screen>(UpcomingScreen) }
    val isDebuggable = platformContext.isDebuggable()

    val isGalleryEnabled by remoteConfig.booleanConfigAsState { isGalleryEnabled() }
    val isRoutesEnabled by remoteConfig.booleanConfigAsState { isRoutesEnabled() }

    val identityState by identityManager.state.collectAsState(IdentityState.Unauthenticated)
    val coroutineScope = rememberCoroutineScope()

    return HomeScreen.State(
        screen = screen,
        isGalleryEnabled = isDebuggable || isGalleryEnabled,
        isRoutesEnabled = isDebuggable || isRoutesEnabled,
        identityState = identityState,
    ) { event ->
        when (event) {
            is HomeScreen.Event.Login -> coroutineScope.launch { identityManager.signIn() }
            is HomeScreen.Event.ChildNav -> navigator.onNavEvent(event.navEvent)
            is HomeScreen.Event.BottomNav -> screen = event.screen
        }
    }
}
