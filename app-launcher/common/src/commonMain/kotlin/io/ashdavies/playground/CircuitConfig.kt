package io.ashdavies.playground

import com.slack.circuit.foundation.CircuitConfig
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
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
