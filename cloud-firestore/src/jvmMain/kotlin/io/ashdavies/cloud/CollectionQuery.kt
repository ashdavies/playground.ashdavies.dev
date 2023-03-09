package io.ashdavies.cloud

import kotlinx.serialization.Serializable

@Serializable
public class CollectionQuery(
    public val orderBy: String = "dateStart",
    public val startAt: String? = null,
    public val limit: Int = 50,
)
