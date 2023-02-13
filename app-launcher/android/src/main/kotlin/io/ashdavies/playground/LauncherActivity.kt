package io.ashdavies.playground

import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext

internal class LauncherActivity : KotlinActivity({
    val initialBackStack = buildInitialBackStack(intent.getStringExtra("route"))
    val circuitConfig = CircuitConfig(defaultComponentContext())

    setContent {
        LauncherApp(
            initialBackStack = initialBackStack,
            circuitConfig = circuitConfig,
        )
    }
},)
