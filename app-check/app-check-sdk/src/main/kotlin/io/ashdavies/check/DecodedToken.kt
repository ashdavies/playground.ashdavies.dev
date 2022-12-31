package io.ashdavies.check

public data class DecodedToken(
    val audience: List<String>,
    val expiresAt: Long,
    val subject: String,
    val issuedAt: Long,
    val issuer: String,
    val appId: String,
)
