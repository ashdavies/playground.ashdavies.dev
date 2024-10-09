package io.ashdavies.http

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpCallValidatorConfig

public inline fun <reified T : Throwable> HttpCallValidatorConfig.throwClientRequestExceptionAs() {
    handleResponseExceptionWithRequest { exception, _ ->
        if (exception is ClientRequestException) throw exception.response.body<T>()
    }
}
