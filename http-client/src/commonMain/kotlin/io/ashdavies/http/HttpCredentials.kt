package io.ashdavies.http

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public val LocalHttpCredentials: ProvidableCompositionLocal<HttpCredentials> = staticCompositionLocalOf {
    error("CompositionLocal LocalHttpCredentials not present")
}

public data class HttpCredentials(
    val apiKey: String,
    val userAgent: String,
)
