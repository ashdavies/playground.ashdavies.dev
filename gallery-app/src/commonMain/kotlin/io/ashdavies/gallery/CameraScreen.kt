package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun CameraScreen(
    state: CameraScreen.State,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    CameraView(modifier) {
        eventSink(CameraScreen.Event.Result(it))
    }
}
