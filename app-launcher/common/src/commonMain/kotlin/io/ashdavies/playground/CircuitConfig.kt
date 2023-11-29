package io.ashdavies.playground

import com.slack.circuit.foundation.Circuit
import io.ashdavies.content.PlatformContext
import io.ashdavies.dominion.DominionPresenterFactory
import io.ashdavies.dominion.DominionUiFactory
import io.ashdavies.gallery.GalleryPresenterFactory
import io.ashdavies.gallery.GalleryUiFactory
import kotlin.coroutines.CoroutineContext

public fun CircuitConfig(
    platformContext: PlatformContext,
    coroutineContext: CoroutineContext,
): Circuit = Circuit.Builder()
    .addPresenterFactories(getPresenterFactories(platformContext, coroutineContext))
    .addUiFactories(getUiFactories(platformContext, coroutineContext))
    .build()

private fun getPresenterFactories(
    platformContext: PlatformContext,
    coroutineContext: CoroutineContext,
) = listOf(
    DominionPresenterFactory(),
    EventsPresenterFactory(),
    GalleryPresenterFactory(
        platformContext = platformContext,
        coroutineContext = coroutineContext,
    ),
    LauncherPresenterFactory(),
)

private fun getUiFactories(
    platformContext: PlatformContext,
    coroutineContext: CoroutineContext,
) = listOf(
    DominionUiFactory(),
    EventsUiFactory(),
    GalleryUiFactory(
        platformContext = platformContext,
        coroutineContext = coroutineContext,
    ),
    LauncherUiFactory(),
)
