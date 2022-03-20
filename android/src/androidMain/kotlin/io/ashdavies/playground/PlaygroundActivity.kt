package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.defaultComponentContext
import io.ashdavies.playground.compose.ComposeActivity
import io.ashdavies.playground.playground.MainScreen

internal class PlaygroundActivity : ComposeActivity() {
    override val content: @Composable () -> Unit = { MainScreen(defaultComponentContext()) }
}
