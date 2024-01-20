package io.ashdavies.playground

import com.slack.circuit.foundation.Circuit
import io.ashdavies.content.PlatformContext
import io.ashdavies.dominion.DominionPresenterFactory
import io.ashdavies.dominion.DominionUiFactory
import io.ashdavies.gallery.GalleryPresenterFactory
import io.ashdavies.gallery.GalleryUiFactory
import io.ashdavies.party.AfterPartyPresenterFactory
import io.ashdavies.party.AfterPartyUiFactory
import io.ashdavies.routes.RoutePresenterFactory
import io.ashdavies.routes.RouteUiFactory

public fun CircuitConfig(context: PlatformContext): Circuit = Circuit.Builder()
    .addPresenterFactories(getPresenterFactories(context))
    .addUiFactories(getUiFactories(context))
    .build()

private fun getPresenterFactories(context: PlatformContext) = listOf(
    AfterPartyPresenterFactory(),
    DominionPresenterFactory(),
    EventsPresenterFactory(),
    GalleryPresenterFactory(context),
    LauncherPresenterFactory(),
    RoutePresenterFactory(),
)

private fun getUiFactories(context: PlatformContext) = listOf(
    AfterPartyUiFactory(),
    DominionUiFactory(),
    EventsUiFactory(),
    GalleryUiFactory(context),
    LauncherUiFactory(),
    RouteUiFactory(),
)
