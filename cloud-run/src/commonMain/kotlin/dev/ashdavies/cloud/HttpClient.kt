package dev.ashdavies.cloud

import dev.ashdavies.cloud.google.GoogleApiException
import io.ashdavies.http.defaultHttpClient
import io.ashdavies.http.throwClientRequestExceptionAs
import io.ktor.client.plugins.HttpCallValidator

internal val httpClient = defaultHttpClient {
    install(HttpCallValidator) {
        throwClientRequestExceptionAs<GoogleApiException>()
    }

    expectSuccess = true
}
