package io.ashdavies.playground

import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext

public fun main(): Unit = singleWindowApplication(title = "Playground") {
    EventsRoot(EventsRootComponent(DefaultComponentContext(DefaultLifecycle())))
}
