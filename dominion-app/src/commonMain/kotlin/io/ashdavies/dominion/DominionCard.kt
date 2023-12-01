package io.ashdavies.dominion

import kotlinx.serialization.Serializable

@Serializable
internal data class DominionCard(
    val expansion: DominionExpansion,
    val name: String,
    val image: String? = null,
)
