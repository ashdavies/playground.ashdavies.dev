package io.ashdavies.playground.network

import kotlinx.serialization.Serializable

@Serializable
internal data class Envelope<T>(
    val results: List<T>,
)