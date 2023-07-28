package io.ashdavies.playground

import com.slack.circuit.foundation.CircuitConfig
import io.ashdavies.content.PlatformContext
import io.ashdavies.dominion.DominionPresenterFactory
import io.ashdavies.dominion.DominionUiFactory
import io.ashdavies.gallery.GalleryPresenterFactory
import io.ashdavies.gallery.GalleryUiFactory

public fun CircuitConfig(context: PlatformContext): CircuitConfig = CircuitConfig.Builder()
    .addPresenterFactories(getPresenterFactories(context))
    .addUiFactories(getUiFactories(context))
    .build()

private fun getPresenterFactories(context: PlatformContext) = listOf(
    DominionPresenterFactory(),
    EventsPresenterFactory(),
    GalleryPresenterFactory(context),
    LauncherPresenterFactory(),
    RatingsPresenterFactory(),
)

private fun getUiFactories(context: PlatformContext) = listOf(
    DominionUiFactory(),
    EventsUiFactory(),
    GalleryUiFactory(context),
    LauncherUiFactory(),
    RatingsUiFactory(),
)
