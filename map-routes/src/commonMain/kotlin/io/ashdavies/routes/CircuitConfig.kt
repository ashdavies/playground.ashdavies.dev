package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.foundation.Circuit
import io.ashdavies.content.PlatformContext

@Composable
public fun rememberCircuit(platformContext: PlatformContext): Circuit = remember(platformContext) {
    Circuit.Builder()
        .addRoutePresenter(platformContext)
        .addRouteUi()
        .build()
}
