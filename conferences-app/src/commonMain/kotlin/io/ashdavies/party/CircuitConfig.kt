package io.ashdavies.party

import com.slack.circuit.foundation.Circuit
import io.ashdavies.content.PlatformContext
import io.ashdavies.events.eventsPresenterFactory
import io.ashdavies.events.eventsUiFactory
import io.ashdavies.gallery.galleryPresenterFactory
import io.ashdavies.gallery.galleryUiFactory

public fun Circuit(context: PlatformContext): Circuit = Circuit.Builder()
    .addPresenterFactories(getPresenterFactories(context))
    .addUiFactories(getUiFactories(context))
    .build()

private fun getPresenterFactories(context: PlatformContext) = listOf(
    afterPartyPresenterFactory(context),
    eventsPresenterFactory(),
    galleryPresenterFactory(context),
)

private fun getUiFactories(context: PlatformContext) = listOf(
    afterPartyUiFactory(context),
    eventsUiFactory(),
    galleryUiFactory(context),
)
