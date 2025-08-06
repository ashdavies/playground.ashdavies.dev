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
import dev.ashdavies.identity.IdentityManager
import dev.ashdavies.identity.IdentityState
import io.ashdavies.tally.adaptive.ListDetailScaffoldScreen
import io.ashdavies.tally.circuit.CircuitScreenKey
import io.ashdavies.tally.config.booleanConfigAsState
import io.ashdavies.tally.config.isGalleryEnabled
import io.ashdavies.tally.config.isRoutesEnabled
import io.ashdavies.tally.upcoming.UpcomingScreen
import kotlinx.coroutines.launch

internal class BottomBarScaffoldPresenter @Inject constructor(
    @Assisted private val navigator: Navigator,
    private val platformContext: PlatformContext,
    private val remoteConfig: RemoteConfig,
    private val identityManager: IdentityManager,
) : Presenter<BottomBarScaffoldScreen.State> {

    @Composable
    override fun present(): BottomBarScaffoldScreen.State {
        var screen by rememberRetained { mutableStateOf<Screen>(ListDetailScaffoldScreen(UpcomingScreen)) }
        val isDebuggable = platformContext.isDebuggable()

        val isGalleryEnabled by remoteConfig.booleanConfigAsState { isGalleryEnabled() }
        val isRoutesEnabled by remoteConfig.booleanConfigAsState { isRoutesEnabled() }

        val identityState by identityManager.state.collectAsState(IdentityState.Unauthenticated)
        val coroutineScope = rememberCoroutineScope()

        return BottomBarScaffoldScreen.State(
            screen = screen,
            isGalleryEnabled = isDebuggable || isGalleryEnabled,
            isRoutesEnabled = isDebuggable || isRoutesEnabled,
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
