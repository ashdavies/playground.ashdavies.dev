package io.ashdavies.playground

public data class AccessToken(
    val extraParameters: Map<String, List<String>>,
    val refreshToken: String?,
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
)
