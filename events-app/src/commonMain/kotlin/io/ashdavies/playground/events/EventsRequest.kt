package io.ashdavies.playground.events

import kotlinx.serialization.Serializable

@Serializable
internal data class EventsRequest(
    val startAt: String?,
    val limit: Int,
)
