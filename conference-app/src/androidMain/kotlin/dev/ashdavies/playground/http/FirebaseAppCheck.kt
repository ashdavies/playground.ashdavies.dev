package dev.ashdavies.playground.http

import dev.ashdavies.http.common.models.XFirebaseAppCheck
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.tasks.await

internal actual val FirebaseAppCheck = createClientPlugin("AppCheck") {
    onRequest { request, _ ->
        request.headers.append(
            name = HttpHeaders.XFirebaseAppCheck,
            value = com.google.firebase.appcheck.FirebaseAppCheck.getInstance()
                .getAppCheckToken(false)
                .await()
                .token,
        )
    }
}
