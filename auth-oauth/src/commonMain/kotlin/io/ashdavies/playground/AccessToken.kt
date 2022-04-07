package io.ashdavies.playground

public data class AccessToken(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val refreshToken: String?
)
