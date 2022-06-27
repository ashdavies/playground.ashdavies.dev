package io.ashdavies.playground

import androidx.compose.runtime.Composable

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

    AppCheck {
        //PlaygroundTheme(content = content)
        content()
    }
}
