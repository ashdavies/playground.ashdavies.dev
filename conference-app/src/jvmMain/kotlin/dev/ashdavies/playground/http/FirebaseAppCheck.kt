package dev.ashdavies.playground.http

import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.createClientPlugin

internal actual val FirebaseAppCheck: ClientPlugin<Unit> = createClientPlugin("AppCheck") {
    // TODO REST implementation
}
