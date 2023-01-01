package io.ashdavies.check

import kotlinx.serialization.Serializable

@Serializable
public data class DecodedToken(
    val audience: List<String>,
    val expiresAt: Long,
    val subject: String,
    val issuedAt: Long,
    val issuer: String,
    val appId: String,
)
