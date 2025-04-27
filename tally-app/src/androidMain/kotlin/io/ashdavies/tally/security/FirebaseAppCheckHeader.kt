package io.ashdavies.tally.security

import com.google.firebase.appcheck.FirebaseAppCheck
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.tasks.await

@Suppress("UnusedReceiverParameter")
private val HttpHeaders.AppCheckToken: String get() = "X-Firebase-AppCheck"

internal val FirebaseAppCheckHeader = createClientPlugin("AppCheck") {
    onRequest { request, _ ->
        request.headers.append(
            name = HttpHeaders.AppCheckToken,
            value = FirebaseAppCheck.getInstance()
                .getAppCheckToken(false)
                .await()
                .token,
        )
    }
}
