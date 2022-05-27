package io.ashdavies.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

public abstract class ComposeActivity(
    private val content: @Composable (ComponentContext) -> Unit,
    private val decorFitsSystemWindows: Boolean = false,
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDecorFitsSystemWindows(window, decorFitsSystemWindows)
        setContent { ComposeScreen { content(defaultComponentContext()) } }
    }
}

@Composable
private fun ComposeScreen(content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
        )
    }

    PlaygroundTheme(content = content)
}
