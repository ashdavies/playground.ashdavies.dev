package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext

internal class PlaygroundActivity : DecomposeActivity() {
    override val root: @Composable (ComponentContext) -> Unit = {
        EventsRoot(EventsRootComponent(it))
    }
}
