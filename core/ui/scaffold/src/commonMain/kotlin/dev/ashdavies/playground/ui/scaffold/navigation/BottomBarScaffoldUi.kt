package dev.ashdavies.playground.ui.scaffold.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dev.ashdavies.playground.ui.snackbar.SnackbarContributor
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
public object BottomBarScaffoldScreen : Screen {
    public sealed interface Event : CircuitUiEvent {
        public data class ChildNav(val navEvent: NavEvent) : Event
        public data class BottomNav(val screen: Screen) : Event
    }

    public sealed interface State : CircuitUiState {
        public object Loading : State

        public data class Ready(
            public val items: PersistentList<Item>,
            public val selectedScreen: Screen,
            public val eventSink: (Event) -> Unit,
        ) : State {

            public data class Item(
                public val selected: Boolean,
                public val screen: Screen,
                public val icon: ImageVector,
                public val label: StringResource,
            )
        }
    }
}

@Inject
@CircuitInject(BottomBarScaffoldScreen::class, AppScope::class)
public class BottomBarScaffoldUi(
    private val snackbarContributor: SnackbarContributor,
) : Ui<BottomBarScaffoldScreen.State> {

    @Composable
    override fun Content(state: BottomBarScaffoldScreen.State, modifier: Modifier) {
        when (state) {
            is BottomBarScaffoldScreen.State.Ready -> {
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(snackbarContributor) {
                    snackbarContributor.source.collect {
                        it(snackbarHostState::showSnackbar)
                    }
                }

                BottomBarScaffoldReady(
                    state = state,
                    snackbarHostState = snackbarHostState,
                    modifier = modifier,
                )
            }

            else -> BottomBarScaffoldLoading()
        }
    }
}
