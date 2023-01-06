package io.ashdavies.playground

import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.arkivanov.decompose.defaultComponentContext

internal class LauncherActivity : KotlinActivity({
    setDecorFitsSystemWindows(window, true)

    val circuitConfig = CircuitConfig(defaultComponentContext())
    val initialBackStack = buildInitialBackStack(
        nextScreen = intent.getStringExtra("route"),
        initialScreen = LauncherScreen,
    )

    setContent {
        LauncherApp(
            initialBackStack = initialBackStack,
            circuitConfig = circuitConfig,
        )
    }
})
