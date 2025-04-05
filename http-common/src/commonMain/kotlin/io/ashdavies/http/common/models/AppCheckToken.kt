package io.ashdavies.http.common.models

import kotlinx.serialization.Serializable

@Serializable
public data class AppCheckToken(
    public val ttlMillis: Long,
    public val token: String,
)
