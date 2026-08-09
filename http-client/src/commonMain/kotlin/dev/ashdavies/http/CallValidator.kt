package dev.ashdavies.http

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpCallValidatorConfig
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import kotlinx.serialization.json.JsonObject

public inline fun <reified T : Throwable> HttpCallValidatorConfig.throwClientRequestExceptionAs() {
    handleResponseExceptionWithRequest { exception, _ ->
        throw when (exception) {
            is ClientRequestException if exception.response.status == HttpStatusCode.NotFound -> IOException(
                "Host '${exception.response.request.url.host}' not found",
            )

            is ServerResponseException if exception.response.status == HttpStatusCode.InternalServerError -> IOException(
                exception.response
                    .body<JsonObject>()
                    .getValue("message")
                    .toString(),
            )

            is ResponseException -> exception.response.body<T>()

            else -> exception
        }
    }
}
