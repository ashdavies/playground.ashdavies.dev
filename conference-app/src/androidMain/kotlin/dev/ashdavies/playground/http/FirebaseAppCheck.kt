package dev.ashdavies.playground.http

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.tasks.await

@Suppress("UnusedReceiverParameter")
private val HttpHeaders.XFirebaseAppCheck: String get() = "X-Firebase-AppCheck"

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
