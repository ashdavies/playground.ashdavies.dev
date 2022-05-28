package io.ashdavies.playground

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
internal fun ComposeScreen(content: @Composable () -> Unit) {
    //val systemUiController = rememberSystemUiController()
    //val useDarkIcons = !isSystemInDarkTheme()

    /*SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
        )
    }*/

    content()
    //PlaygroundTheme(content = content)
}
