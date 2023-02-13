package io.ashdavies.playground

import com.slack.circuit.CircuitConfig
import com.slack.circuit.Presenter
import com.slack.circuit.Ui
import io.ashdavies.dominion.DominionPresenterFactory
import io.ashdavies.dominion.DominionUiFactory

public fun CircuitConfig(): CircuitConfig = CircuitConfig.Builder()
    .addPresenterFactories(getPresenterFactories())
    .addUiFactories(getUiFactories())
    .build()

private fun getPresenterFactories(): List<Presenter.Factory> = listOf(
    LauncherPresenterFactory(),
    EventsPresenterFactory(),
    DominionPresenterFactory(),
)

private fun getUiFactories(): List<Ui.Factory> = listOf(
    LauncherUiFactory(),
    EventsUiFactory(),
    DominionUiFactory(),
)
