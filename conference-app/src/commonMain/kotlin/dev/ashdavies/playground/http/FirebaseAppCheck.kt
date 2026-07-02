package dev.ashdavies.playground.http

import io.ktor.client.plugins.api.ClientPlugin

internal expect val FirebaseAppCheck: ClientPlugin<Unit>
