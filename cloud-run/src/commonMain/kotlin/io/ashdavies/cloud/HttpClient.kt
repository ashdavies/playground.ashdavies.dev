package io.ashdavies.cloud

import io.ashdavies.cloud.google.GoogleApiException
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.http.publicStorage
import io.ashdavies.http.throwClientRequestExceptionAs
import io.ashdavies.io.resolveCacheDir
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.cache.HttpCache

internal val httpClient = HttpClient {
    DefaultHttpConfiguration()

    install(HttpCache) {
        publicStorage(PlatformContext.Default.resolveCacheDir())
    }

    install(HttpCallValidator) {
        throwClientRequestExceptionAs<GoogleApiException>()
    }

    expectSuccess = true
}
