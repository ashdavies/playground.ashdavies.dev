package dev.ashdavies.playground.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject

@Inject
@CircuitInject(RoutesScreen::class, AppScope::class)
internal class RoutesUi() : Ui<RoutesScreen.State> {

    @Composable
    override fun Content(state: RoutesScreen.State, modifier: Modifier) {
        RoutesMap(
            state = state.mapState,
            onEndPosition = { state.eventSink(RoutesScreen.Event.OnEndPosition(it)) },
            modifier = modifier,
        )
    }
}
