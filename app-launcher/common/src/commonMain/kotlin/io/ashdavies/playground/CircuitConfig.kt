package io.ashdavies.playground

import com.slack.circuit.foundation.Circuit
import io.ashdavies.content.PlatformContext
import io.ashdavies.dominion.dominionPresenterFactory
import io.ashdavies.dominion.dominionUiFactory
import io.ashdavies.routes.routePresenterFactory
import io.ashdavies.routes.routeUiFactory

public fun Circuit(context: PlatformContext): Circuit = Circuit.Builder()
    .addPresenterFactories(getPresenterFactories(context))
    .addUiFactories(getUiFactories(context))
    .build()

private fun getPresenterFactories(context: PlatformContext) = listOf(
    dominionPresenterFactory(),
    launcherPresenterFactory(),
    routePresenterFactory(context),
)

private fun getUiFactories(context: PlatformContext) = listOf(
    dominionUiFactory(),
    launcherUiFactory(),
    routeUiFactory(context),
)
