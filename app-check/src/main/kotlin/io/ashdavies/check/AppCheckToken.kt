package io.ashdavies.check

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
internal data class AppCheckToken(val token: String, val ttlMillis: Int)

@Serializable
internal data class AppCheckTokenOptions(
    val issuedAt: Instant = Clock.System.now(),
    val authenticityToken: String? = null,
    val issuer: String = APP_CHECK_ISSUER,
    val ttlMillis: Int? = null,
    val expiresAt: Instant,
    val appId: String
)
