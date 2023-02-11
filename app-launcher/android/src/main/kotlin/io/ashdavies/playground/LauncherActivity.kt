package io.ashdavies.playground

import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext

internal class LauncherActivity : KotlinActivity({
    val circuitConfig = CircuitConfig(defaultComponentContext())
    val initialBackStack = buildInitialBackStack(intent.getStringExtra("route"))

    setContent {
        LauncherApp(
            initialBackStack = initialBackStack,
            circuitConfig = circuitConfig,
        )
    }
},)
