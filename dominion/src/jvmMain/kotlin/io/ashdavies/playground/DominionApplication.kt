package io.ashdavies.playground

import androidx.compose.runtime.remember
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext

public fun main(): Unit = singleWindowApplication(title = "Playground") {
    val root: DominionRoot = remember {
        val context = DefaultComponentContext(DefaultLifecycle())
        DominionRootComponent(context)
    }

    DominionRoot(root)
}
