package io.ashdavies.http

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpCallValidator

public inline fun <reified T : Exception> HttpCallValidator.Config.throwClientRequestExceptionAs() {
    handleResponseExceptionWithRequest { exception, _ ->
        if (exception is ClientRequestException) throw exception.response.body<T>()
    }
}
