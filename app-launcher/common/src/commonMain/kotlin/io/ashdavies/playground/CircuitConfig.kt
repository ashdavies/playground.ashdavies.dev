package io.ashdavies.playground

import com.slack.circuit.foundation.CircuitConfig
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.dominion.DominionPresenterFactory
import io.ashdavies.dominion.DominionUiFactory
import io.ashdavies.gallery.GalleryPresenterFactory
import io.ashdavies.gallery.GalleryUiFactory

public fun CircuitConfig(): CircuitConfig = CircuitConfig.Builder()
    .addPresenterFactories(getPresenterFactories())
    .addUiFactories(getUiFactories())
    .build()

private fun getPresenterFactories(): List<Presenter.Factory> = listOf(
    DominionPresenterFactory(),
    EventsPresenterFactory(),
    GalleryPresenterFactory(),
    LauncherPresenterFactory(),
    RatingsPresenterFactory(),
)

private fun getUiFactories(): List<Ui.Factory> = listOf(
    DominionUiFactory(),
    EventsUiFactory(),
    GalleryUiFactory(),
    LauncherUiFactory(),
    RatingsUiFactory(),
)
