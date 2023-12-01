package io.ashdavies.dominion

import kotlinx.serialization.Serializable

@Serializable
internal data class DominionExpansion(
    val name: String,
    val image: String,
)
