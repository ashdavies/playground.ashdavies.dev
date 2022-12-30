package io.ashdavies.check

import kotlinx.datetime.Instant

public data class DecodedToken(
    val audience: List<String>,
    val expiresAt: Instant,
    val issuedAt: Instant,
    val subject: String,
    val issuer: String,
    val appId: String,
)
