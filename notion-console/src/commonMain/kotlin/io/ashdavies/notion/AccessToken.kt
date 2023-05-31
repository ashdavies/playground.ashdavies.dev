package io.ashdavies.notion

internal data class AccessToken(
    val extraParameters: Map<String, List<String>>,
    val refreshToken: String?,
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
)

internal fun AccessToken.firstOrThrow(name: String): String =
    extraParameters
        .getValue(name)
        .first()
