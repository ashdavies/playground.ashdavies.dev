package io.ashdavies.playground

import com.slack.circuit.foundation.CircuitConfig
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.dominion.DominionPresenterFactory
import io.ashdavies.dominion.DominionUiFactory
import io.ashdavies.gallery.GalleryPresenterFactory
import io.ashdavies.gallery.GalleryUiFactory

private val presenterFactories: List<Presenter.Factory>
    get() = listOf(
        DominionPresenterFactory(),
        EventsPresenterFactory(),
        GalleryPresenterFactory(),
        LauncherPresenterFactory(),
        RatingsPresenterFactory(),
    )

private val uiFactories: List<Ui.Factory>
    get() = listOf(
        DominionUiFactory(),
        EventsUiFactory(),
        GalleryUiFactory(),
        LauncherUiFactory(),
        RatingsUiFactory(),
    )

public fun CircuitConfig(): CircuitConfig = CircuitConfig.Builder()
    .addPresenterFactories(presenterFactories)
    .addUiFactories(uiFactories)
    .build()
