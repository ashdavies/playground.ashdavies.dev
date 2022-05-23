package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext

internal class DominionActivity : ComposeActivity() {
    override val content: @Composable (ComponentContext) -> Unit = { DominionRoot(it) }
}
