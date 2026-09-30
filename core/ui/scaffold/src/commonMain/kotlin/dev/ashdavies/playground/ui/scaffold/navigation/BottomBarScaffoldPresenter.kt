package dev.ashdavies.playground.ui.scaffold.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.playground.ui.scaffold.adaptive.ListDetailScaffoldScreen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.toPersistentList

internal class BottomBarScaffoldPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val bottomBarScaffoldConfig: BottomBarScaffoldConfig,
    private val bottomBarItemsProvider: suspend () -> List<Pair<Screen, BottomBarScaffoldMetadata>>,
) : Presenter<BottomBarScaffoldScreen.State> {

    @Composable
    override fun present(): BottomBarScaffoldScreen.State {
        var selectedScreen by retain { mutableStateOf<Screen>(ListDetailScaffoldScreen(bottomBarScaffoldConfig.initialScreen)) }

        val bottomBarItemsState = produceState<List<Pair<Screen, BottomBarScaffoldMetadata>>?>(null) {
            value = bottomBarItemsProvider()
        }

        return when (val bottomBarItems = bottomBarItemsState.value) {
            null -> BottomBarScaffoldScreen.State.Loading

            else -> BottomBarScaffoldScreen.State.Ready(
                items = bottomBarItems.map {
                    BottomBarScaffoldScreen.State.Ready.Item(
                        screen = it.first,
                        label = it.second.label,
                        icon = it.second.icon,
                        selected = selectedScreen == it.first,
                    )
                }.toPersistentList(),
                selectedScreen = selectedScreen,
            ) { event ->
                when (event) {
                    is BottomBarScaffoldScreen.Event.ChildNav -> navigator.onNavEvent(event.navEvent)
                    is BottomBarScaffoldScreen.Event.BottomNav -> selectedScreen = event.screen
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(BottomBarScaffoldScreen::class, AppScope::class)
    fun interface Factory : (Navigator) -> BottomBarScaffoldPresenter {
        override operator fun invoke(navigator: Navigator): BottomBarScaffoldPresenter
    }
}
