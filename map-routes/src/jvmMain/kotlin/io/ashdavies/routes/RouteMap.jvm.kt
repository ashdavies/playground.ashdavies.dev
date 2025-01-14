package io.ashdavies.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal actual fun RouteMap(
    state: RouteMapState,
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
