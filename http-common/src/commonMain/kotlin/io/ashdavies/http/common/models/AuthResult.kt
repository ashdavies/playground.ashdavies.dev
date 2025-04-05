package io.ashdavies.http.common.models

import kotlinx.serialization.Serializable

@Serializable
public data class AuthResult(
    public val idToken: String?,
    public val refreshToken: String?,
    public val expiresIn: Long?,
)
