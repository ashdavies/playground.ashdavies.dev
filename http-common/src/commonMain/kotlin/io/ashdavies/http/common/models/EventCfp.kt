package io.ashdavies.http.common.models

import kotlinx.serialization.Serializable

@Serializable
public data class EventCfp(
    public val start: String?,
    public val end: String?,
    public val site: String?,
)
