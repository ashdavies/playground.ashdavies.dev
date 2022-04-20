package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext

@Composable
internal fun PlaygroundScreen(componentContext: ComponentContext) {
    PlaygroundScreen {
        EventsRoot(EventsRootComponent(componentContext))
    }
}
