package dev.ashdavies.playground.http

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.AuthProvider

internal expect fun createAppCheckAuthProvider(
    appId: String,
    tokenClient: HttpClient? = null,
): AuthProvider
