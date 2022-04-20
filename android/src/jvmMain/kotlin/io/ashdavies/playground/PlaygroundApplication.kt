package io.ashdavies.playground

import androidx.compose.runtime.remember
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext

public fun main(): Unit = singleWindowApplication(title = "Playground") {
    val root: PlaygroundRoot = remember(Unit) {
        PlaygroundRootComponent(DefaultComponentContext(DefaultLifecycle()))
    }

    PlaygroundRoot(root)
}
