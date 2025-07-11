package io.ashdavies.cloud.google

import kotlinx.serialization.Serializable

@Serializable
internal data class GoogleApiError(
    val code: Int,
    val message: String,
    val status: String,
)
