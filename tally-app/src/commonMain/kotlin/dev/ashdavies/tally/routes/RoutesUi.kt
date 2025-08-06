package dev.ashdavies.tally.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.ashdavies.tally.circuit.CircuitScreenKey

@CircuitScreenKey(RoutesScreen::class)
@ContributesIntoMap(AppScope::class, binding<Ui<*>>())
internal class RoutesUi @Inject constructor() : Ui<RoutesScreen.State> {

    @Composable
    override fun Content(state: RoutesScreen.State, modifier: Modifier) {
        RoutesMap(
            state = state.mapState,
            onEndPosition = { state.eventSink(RoutesScreen.Event.OnEndPosition(it)) },
            modifier = modifier,
        )
    }
}
