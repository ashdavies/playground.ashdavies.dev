package io.ashdavies.playground

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.resourcesFetcher

fun main() = singleWindowApplication(title = "Playground") {
    val root: PlaygroundRoot = remember {
        val context = DefaultComponentContext(DefaultLifecycle())
        PlaygroundRootComponent(context)
    }

    val config = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
    }

    CompositionLocalProvider(LocalKamelConfig provides config) {
        PlaygroundRoot(root)
    }
}
