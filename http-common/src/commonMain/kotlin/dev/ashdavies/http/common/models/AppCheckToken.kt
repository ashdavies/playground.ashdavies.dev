package dev.ashdavies.http.common.models

import kotlinx.serialization.Serializable

@Serializable
public data class AppCheckToken(
    public val token: String,
)
