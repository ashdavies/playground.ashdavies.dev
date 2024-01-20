package io.ashdavies.dominion

import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
internal data class DominionCard(
    val expansion: DominionExpansion,
    val name: String,
    val image: String? = null,
) : Parcelable
