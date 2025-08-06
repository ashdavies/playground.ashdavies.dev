package dev.ashdavies.cloud

import dev.ashdavies.cloud.google.GoogleApiException
import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.http.throwClientRequestExceptionAs
import io.ktor.client.plugins.HttpCallValidator

internal val httpClient = defaultHttpClient {
    install(HttpCallValidator) {
        throwClientRequestExceptionAs<GoogleApiException>()
    }

    expectSuccess = true
}
