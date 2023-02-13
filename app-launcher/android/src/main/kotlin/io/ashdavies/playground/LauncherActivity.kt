package io.ashdavies.playground

import androidx.activity.compose.setContent

internal class LauncherActivity : KotlinActivity({
    val initialBackStack = buildInitialBackStack(intent.getStringExtra("route"))
    val circuitConfig = CircuitConfig()

    setContent {
        LauncherApp(
            initialBackStack = initialBackStack,
            circuitConfig = circuitConfig,
        )
    }
},)
