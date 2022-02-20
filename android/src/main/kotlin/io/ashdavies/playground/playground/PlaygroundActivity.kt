package io.ashdavies.playground.playground

import androidx.compose.runtime.Composable
import io.ashdavies.playground.compose.ComposeActivity

internal class PlaygroundActivity : ComposeActivity() {
    override val content: @Composable () -> Unit = { MainScreen() }
}
