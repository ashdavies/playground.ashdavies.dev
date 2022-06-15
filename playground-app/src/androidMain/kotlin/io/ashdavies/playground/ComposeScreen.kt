package io.ashdavies.playground

import androidx.compose.runtime.Composable
import io.ashdavies.check.AppCheck

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

@Composable
internal fun VerifiedScreen(content: @Composable () -> Unit) {
    AppCheck(verify = true) { ComposeScreen(content) }
}
