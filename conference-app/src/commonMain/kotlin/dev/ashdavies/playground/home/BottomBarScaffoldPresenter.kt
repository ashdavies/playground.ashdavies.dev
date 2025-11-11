package dev.ashdavies.playground.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.identity.IdentityManager
import dev.ashdavies.identity.IdentityState
import dev.ashdavies.playground.adaptive.ListDetailScaffoldScreen
import dev.ashdavies.playground.circuit.CircuitScreenKey
import dev.ashdavies.playground.config.booleanConfigAsState
import dev.ashdavies.playground.config.isGalleryEnabled
import dev.ashdavies.playground.config.isPastEventsEnabled
import dev.ashdavies.playground.config.isRoutesEnabled
import dev.ashdavies.playground.upcoming.UpcomingScreen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import kotlinx.coroutines.launch

internal class BottomBarScaffoldPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val remoteConfig: RemoteConfig,
    private val identityManager: IdentityManager,
) : Presenter<BottomBarScaffoldScreen.State> {

    @Composable
    override fun present(): BottomBarScaffoldScreen.State {
        val isGalleryEnabled by remoteConfig.booleanConfigAsState { isGalleryEnabled() }
        val isPastEventsEnabled by remoteConfig.booleanConfigAsState { isPastEventsEnabled() }
        val isRoutesEnabled by remoteConfig.booleanConfigAsState { isRoutesEnabled() }

        var screen by rememberRetained { mutableStateOf<Screen>(ListDetailScaffoldScreen(UpcomingScreen)) }
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
    @CircuitScreenKey(BottomBarScaffoldScreen::class)
    @ContributesIntoMap(AppScope::class, binding<(Navigator) -> Presenter<*>>())
    interface Factory : (Navigator) -> BottomBarScaffoldPresenter
}
