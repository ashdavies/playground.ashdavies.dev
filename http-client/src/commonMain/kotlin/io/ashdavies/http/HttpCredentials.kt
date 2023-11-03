package io.ashdavies.http

import androidx.compose.runtime.staticCompositionLocalOf

public val LocalHttpCredentials = staticCompositionLocalOf<HttpCredentials> {
    error("CompositionLocal LocalHttpCredentials not present")
}

public data class HttpCredentials(
    val apiKey: String,
    val userAgent: String,
)
