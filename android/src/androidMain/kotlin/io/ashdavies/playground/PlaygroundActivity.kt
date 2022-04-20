package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.defaultComponentContext
import io.ashdavies.playground.compose.ComposeActivity

internal class PlaygroundActivity : ComposeActivity() {
    override val content = @Composable { PlaygroundScreen(defaultComponentContext()) }
}
