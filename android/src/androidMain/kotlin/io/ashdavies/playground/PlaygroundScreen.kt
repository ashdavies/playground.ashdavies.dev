package io.ashdavies.playground

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.decompose.ComponentContext
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.config.resourcesIdMapper

@Composable
internal fun PlaygroundScreen(componentContext: ComponentContext) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
        )
    }

    val root: PlaygroundRoot = remember {
        PlaygroundRootComponent(componentContext)
    }

    val config = KamelConfig {
        resourcesIdMapper(LocalContext.current)
        resourcesFetcher(LocalContext.current)
        takeFrom(KamelConfig.Default)
    }

    CompositionLocalProvider(LocalKamelConfig provides config) {
        ProvideWindowInsets {
            PlaygroundRoot(root)
        }
    }
}
