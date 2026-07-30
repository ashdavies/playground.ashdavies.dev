package dev.ashdavies.http

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpCallValidatorConfig
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException

public inline fun <reified T : Throwable> HttpCallValidatorConfig.throwClientRequestExceptionAs() {
    handleResponseExceptionWithRequest { exception, _ ->
        throw when (exception) {
            is ClientRequestException -> when {
                exception.response.status == HttpStatusCode.NotFound ->
                    IOException("Host '${exception.response.request.url.host}' not found")

                else ->
                    exception.response.body<T>()
            }

            else ->
                exception
        }
    }
}
