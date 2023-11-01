package io.ashdavies.gallery

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

private val DefaultHeaders = headersOf(HttpHeaders.ContentType, "application/json")

private val HttpRequestData.path: String
    get() = url.encodedPath.substring(1)

private val Headers.contentLength: String
    get() = requireNotNull(get(HttpHeaders.ContentLength))

internal fun InMemoryHttpClientEngine(initialValue: List<String>): HttpClientEngine {
    val values = initialValue.toMutableList()

    return MockEngine { request ->
        when {
            request.method == HttpMethod.Get && request.path.isEmpty() -> {
                respond(ByteReadChannel("[${values.joinToString()}]"), headers = DefaultHeaders)
            }

            request.method == HttpMethod.Post && request.path.isNotEmpty() -> {
                require(request.body.contentLength == request.headers.contentLength.toLong())
                values += request.path

                respond(ByteReadChannel.Empty, headers = DefaultHeaders)
            }

            request.method == HttpMethod.Put && request.path.isNotEmpty() -> {
                require(request.body.contentLength == 0L)
                values += request.path

                respond(ByteReadChannel.Empty, headers = DefaultHeaders)
            }

            request.method == HttpMethod.Delete && request.path.isNotEmpty() -> {
                require(request.body.contentLength == 0L)
                values -= request.path

                respond(ByteReadChannel.Empty, headers = DefaultHeaders)
            }

            else -> error("Unhandled request: $request")
        }
    }
}
