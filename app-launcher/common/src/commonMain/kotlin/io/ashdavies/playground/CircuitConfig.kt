package io.ashdavies.playground

import com.slack.circuit.foundation.Circuit
import io.ashdavies.content.PlatformContext
import io.ashdavies.dominion.dominionPresenterFactory
import io.ashdavies.dominion.dominionUiFactory
import io.ashdavies.events.eventsPresenterFactory
import io.ashdavies.events.eventsUiFactory
import io.ashdavies.gallery.galleryPresenterFactory
import io.ashdavies.gallery.galleryUiFactory
import io.ashdavies.party.afterPartyPresenterFactory
import io.ashdavies.party.afterPartyUiFactory
import io.ashdavies.routes.routePresenterFactory
import io.ashdavies.routes.routeUiFactory

public fun Circuit(context: PlatformContext): Circuit = Circuit.Builder()
    .addPresenterFactories(getPresenterFactories(context))
    .addUiFactories(getUiFactories(context))
    .build()

private fun getPresenterFactories(context: PlatformContext) = listOf(
    afterPartyPresenterFactory(),
    dominionPresenterFactory(),
    eventsPresenterFactory(),
    galleryPresenterFactory(context),
    launcherPresenterFactory(),
    routePresenterFactory(context),
)

private fun getUiFactories(context: PlatformContext) = listOf(
    afterPartyUiFactory(context),
    dominionUiFactory(),
    eventsUiFactory(),
    galleryUiFactory(context),
    launcherUiFactory(),
    routeUiFactory(context),
)
