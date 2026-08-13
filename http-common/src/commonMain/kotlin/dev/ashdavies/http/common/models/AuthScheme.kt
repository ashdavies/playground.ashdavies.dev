package dev.ashdavies.http.common.models

import io.ktor.http.auth.AuthScheme

@Suppress("UnusedReceiverParameter")
public val AuthScheme.AppCheck: String
    get() = "AppCheck"
