package dev.ashdavies.playground.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.getBoolean
import dev.ashdavies.identity.IdentityManager
import dev.ashdavies.identity.IdentityState
import dev.ashdavies.playground.adaptive.ListDetailScaffoldScreen
import dev.ashdavies.playground.event.EventScreen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.launch

internal class BottomBarScaffoldPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val remoteConfig: RemoteConfig,
    private val identityManager: IdentityManager,
) : Presenter<BottomBarScaffoldScreen.State> {

    @Composable
    override fun present(): BottomBarScaffoldScreen.State {
        val isGalleryEnabled by produceState(false) { remoteConfig.isGalleryEnabled() }
        val isPastEventsEnabled by produceState(false) { remoteConfig.isPastEventsEnabled() }
        val isRoutesEnabled by produceState(false) { remoteConfig.isRoutesEnabled() }

        var screen by retain { mutableStateOf<Screen>(ListDetailScaffoldScreen(EventScreen.List())) }
        val identityState by identityManager.state.collectAsState(IdentityState.Unauthenticated)
        val coroutineScope = rememberCoroutineScope()

        return BottomBarScaffoldScreen.State(
            screen = screen,
            isGalleryEnabled = isGalleryEnabled,
            isRoutesEnabled = isRoutesEnabled,
            isPastEventsEnabled = isPastEventsEnabled,
            identityState = identityState,
        ) { event ->
            when (event) {
                is BottomBarScaffoldScreen.Event.Login -> coroutineScope.launch { identityManager.signIn() }
                is BottomBarScaffoldScreen.Event.ChildNav -> navigator.onNavEvent(event.navEvent)
                is BottomBarScaffoldScreen.Event.BottomNav -> screen = event.screen
            }
        }
    }

    @AssistedFactory
    @CircuitInject(BottomBarScaffoldScreen::class, AppScope::class)
    fun interface Factory : (Navigator) -> BottomBarScaffoldPresenter {
        override operator fun invoke(navigator: Navigator): BottomBarScaffoldPresenter
    }
}

private suspend fun RemoteConfig.isGalleryEnabled() = getBoolean("gallery_enabled")

private suspend fun RemoteConfig.isPastEventsEnabled() = getBoolean("past_events_enabled")

private suspend fun RemoteConfig.isRoutesEnabled() = getBoolean("routes_enabled")
