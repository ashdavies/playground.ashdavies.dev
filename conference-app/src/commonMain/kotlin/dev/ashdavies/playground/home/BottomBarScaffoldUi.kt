package dev.ashdavies.playground.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dev.ashdavies.identity.IdentityState
import dev.ashdavies.parcelable.Parcelable
import dev.ashdavies.parcelable.Parcelize
import dev.ashdavies.playground.activity.FullyDrawnReporter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Parcelize
@Serializable
internal object BottomBarScaffoldScreen : Parcelable, Screen {
    sealed interface Event : CircuitUiEvent {
        data class ChildNav(val navEvent: NavEvent) : Event
        data class BottomNav(val screen: Screen) : Event

        data object Login : Event
    }

    sealed interface State : CircuitUiState {
        object Loading : State

        data class Ready(
            val items: PersistentList<Item>,
            val identityState: IdentityState,
            val selectedScreen: Screen,
            val eventSink: (Event) -> Unit,
        ) : State {

            data class Item(
                val selected: Boolean,
                val screen: Screen,
                val icon: ImageVector,
                val label: StringResource,
            )
        }
    }
}

@Inject
@CircuitInject(BottomBarScaffoldScreen::class, AppScope::class)
internal class BottomBarScaffoldUi(
    private val fullyDrawnReporter: FullyDrawnReporter,
) : Ui<BottomBarScaffoldScreen.State> {

    @Composable
    override fun Content(state: BottomBarScaffoldScreen.State, modifier: Modifier) {
        when (state) {
            is BottomBarScaffoldScreen.State.Ready -> BottomBarScaffoldReady(
                state = state,
                modifier = modifier,
            )

            else -> BottomBarScaffoldLoading()
        }

        LaunchedEffect(Unit) {
            fullyDrawnReporter.reportFullyDrawn()
        }
    }
}
