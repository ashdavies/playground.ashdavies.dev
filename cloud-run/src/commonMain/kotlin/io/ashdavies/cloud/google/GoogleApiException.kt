package io.ashdavies.cloud.google

import kotlinx.serialization.Serializable

@Serializable
internal data class GoogleApiException(
    val error: GoogleApiError,
) : Exception(error.message)
