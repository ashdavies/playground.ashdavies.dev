package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.defaultComponentContext

internal class DominionActivity : ComposeActivity() {
    override val content = @Composable { DominionScreen(defaultComponentContext()) }
}
