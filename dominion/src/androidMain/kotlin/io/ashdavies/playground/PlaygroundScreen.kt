package io.ashdavies.playground

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
internal fun DominionScreen(componentContext: ComponentContext) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
        )
    }

    val root: DominionRoot = remember {
        DominionRootComponent(componentContext)
    }

    ProvideWindowInsets {
        DominionRoot(root)
    }
}
