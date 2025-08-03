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
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ashdavies.config.RemoteConfig
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.isDebuggable
import io.ashdavies.identity.IdentityManager
import io.ashdavies.identity.IdentityState
import io.ashdavies.tally.circuit.CircuitScreenKey
import io.ashdavies.tally.config.booleanConfigAsState
import io.ashdavies.tally.config.isGalleryEnabled
import io.ashdavies.tally.config.isRoutesEnabled
import io.ashdavies.tally.upcoming.UpcomingScreen
import kotlinx.coroutines.launch

internal class HomePresenter @Inject constructor(
    @Assisted private val screen: HomeScreen,
    @Assisted private val navigator: Navigator,
    private val platformContext: PlatformContext,
    private val remoteConfig: RemoteConfig,
    private val identityManager: IdentityManager,
) : Presenter<HomeScreen.State> {

    @Composable
    override fun present(): HomeScreen.State {
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

    @AssistedFactory
    @CircuitScreenKey(HomeScreen::class)
    @ContributesIntoMap(AppScope::class, binding<(Screen, Navigator) -> Presenter<*>>())
    interface Factory : (HomeScreen, Navigator) -> HomePresenter
}
