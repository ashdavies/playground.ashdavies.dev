package io.ashdavies.cloud

import kotlinx.serialization.Serializable

@Serializable
internal data class GoogleApiException(
    val error: Error,
) : Exception(error.message) {

    @Serializable
    data class Error(
        val code: Int,
        val message: String,
        val status: String,
    )
}
