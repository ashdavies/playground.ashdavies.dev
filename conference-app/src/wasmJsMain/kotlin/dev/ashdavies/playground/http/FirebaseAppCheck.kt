package dev.ashdavies.playground.http

import dev.ashdavies.http.common.models.XFirebaseAppCheck
import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.await

internal actual val FirebaseAppCheck: ClientPlugin<Unit> = createClientPlugin("AppCheck") {
    onRequest { request, _ ->
        request.headers.append(
            name = HttpHeaders.XFirebaseAppCheck,
            value = getAppCheckToken(false)
                .await()
                .token,
        )
    }
}
