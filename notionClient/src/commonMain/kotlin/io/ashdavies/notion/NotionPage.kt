package io.ashdavies.notion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotionPage<T : Any>(
    @SerialName("object") val type: NotionType,
    @SerialName("results") val results: List<T>,
    @SerialName("next_cursor") val nextCursor: String?,
    @SerialName("has_more") val hasMore: Boolean,
)
