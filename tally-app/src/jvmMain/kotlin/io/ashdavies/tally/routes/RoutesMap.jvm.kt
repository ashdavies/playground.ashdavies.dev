package io.ashdavies.tally.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.ashdavies.tally.routes.LatLng
import io.ashdavies.tally.routes.RoutesMapState

@Composable
internal actual fun RoutesMap(
    state: RoutesMapState,
    onEndPosition: (LatLng) -> Unit,
    modifier: Modifier,
) {
    Box(Modifier.fillMaxSize()) {
        Text(
            text = "Operation Not Implemented",
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
