package io.ashdavies.playground

internal data class RatingsItem(
    val id: String,
    val name: String,
    val ignored: Boolean,
    val score: Double,
    val url: String,
)
