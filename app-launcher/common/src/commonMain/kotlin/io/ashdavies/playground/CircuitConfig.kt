package io.ashdavies.playground

import com.arkivanov.decompose.ComponentContext
import com.slack.circuit.CircuitConfig
import com.slack.circuit.Presenter
import com.slack.circuit.Ui

public fun CircuitConfig(componentContext: ComponentContext): CircuitConfig = CircuitConfig.Builder()
    .addPresenterFactories(getPresenterFactories())
    .addUiFactories(getUiFactories(componentContext))
    .build()

private fun getPresenterFactories(): List<Presenter.Factory> = listOf(
    LauncherPresenterFactory(),
    EventsPresenterFactory(),
)

private fun getUiFactories(componentContext: ComponentContext): List<Ui.Factory> = listOf(
    LauncherUiFactory(componentContext),
    EventsUiFactory(),
)
