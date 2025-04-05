package io.ashdavies.http.common.models

import kotlinx.serialization.Serializable

@Serializable
public data class DecodedToken(
    public val audience: List<String>,
    public val expiresAt: Long,
    public val issuedAt: Long,
    public val subject: String,
    public val issuer: String,
    public val appId: String,
)
