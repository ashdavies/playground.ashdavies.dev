package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext

internal class EventsActivity : ComposeActivity() {
    override val content: @Composable (ComponentContext) -> Unit = { EventsRoot(it) }
}
