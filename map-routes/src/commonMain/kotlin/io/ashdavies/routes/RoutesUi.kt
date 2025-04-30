package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun RoutesScreen(
    state: RoutesScreen.State,
    modifier: Modifier = Modifier,
) {
    RoutesMap(
        state = state.mapState,
        onEndPosition = { state.eventSink(RoutesScreen.Event.OnEndPosition(it)) },
        modifier = modifier,
    )
}
