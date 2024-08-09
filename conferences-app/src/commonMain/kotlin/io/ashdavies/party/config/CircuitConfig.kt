package io.ashdavies.party.config

import com.slack.circuit.foundation.Circuit
import io.ashdavies.content.PlatformContext
import io.ashdavies.party.home.homePresenterFactory
import io.ashdavies.party.home.homeUiFactory
import io.ashdavies.party.events.eventsPresenterFactory
import io.ashdavies.party.events.eventsUiFactory
import io.ashdavies.party.gallery.galleryPresenterFactory
import io.ashdavies.party.gallery.galleryUiFactory

public fun Circuit(context: PlatformContext): Circuit = Circuit.Builder()
    .addPresenterFactories(getPresenterFactories(context))
    .addUiFactories(getUiFactories(context))
    .build()

private fun getPresenterFactories(context: PlatformContext) = listOf(
    homePresenterFactory(context),
    eventsPresenterFactory(),
    galleryPresenterFactory(context),
)

private fun getUiFactories(context: PlatformContext) = listOf(
    homeUiFactory(context),
    eventsUiFactory(),
    galleryUiFactory(context),
)
