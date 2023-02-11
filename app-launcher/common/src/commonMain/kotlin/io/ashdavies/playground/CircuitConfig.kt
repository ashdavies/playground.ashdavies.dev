package io.ashdavies.playground

import com.arkivanov.decompose.ComponentContext
import com.slack.circuit.CircuitConfig

public fun CircuitConfig(componentContext: ComponentContext): CircuitConfig {
    return CircuitConfig.Builder()
        .addPresenterFactory(LauncherPresenterFactory())
        .addUiFactory(LauncherUiFactory(componentContext))
        .build()
}
