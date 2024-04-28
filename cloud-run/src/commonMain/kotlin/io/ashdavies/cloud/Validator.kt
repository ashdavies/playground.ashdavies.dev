package io.ashdavies.cloud

import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpCallValidator
import kotlinx.serialization.Serializable

internal fun HttpClientConfig<*>.installCallValidator() {
    install(HttpCallValidator) {
        handleResponseExceptionWithRequest { exception, _ ->
            if (exception is ClientRequestException) {
                throw exception.response.body<GoogleApisException>()
            }
        }
    }

    expectSuccess = true
}

@Serializable
internal data class GoogleApisException(val error: Error) : Exception(error.message) {

    @Serializable
    data class Error(
        val code: Int,
        val message: String,
        val status: String,
    )
}
