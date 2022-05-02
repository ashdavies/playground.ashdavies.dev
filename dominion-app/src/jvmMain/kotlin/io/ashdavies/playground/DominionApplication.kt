package io.ashdavies.playground

import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext

public fun main() {
    singleWindowApplication(
        state = WindowState(size = DpSize(450.dp, 975.dp)),
        title = "Playground"
    ) {
        val root: DominionRoot = remember {
            val context = DefaultComponentContext(DefaultLifecycle())
            DominionRootComponent(context)
        }

        PlaygroundTheme {
            DominionRoot(root)
        }
    }
}
