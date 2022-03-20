package io.ashdavies.playground

import androidx.compose.runtime.remember
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext

fun main() = singleWindowApplication(title = "Playground") {
    val root: PlaygroundRoot = remember {
        val context = DefaultComponentContext(DefaultLifecycle())
        PlaygroundRootComponent(context)
    }

    PlaygroundRoot(root)
}
