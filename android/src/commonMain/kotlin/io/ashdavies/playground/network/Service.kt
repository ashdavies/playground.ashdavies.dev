package io.ashdavies.playground.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent
import io.ktor.http.content.OutgoingContent.NoContent
import kotlin.properties.ReadOnlyProperty

interface Service

interface ServiceOperator<T, R> {
    suspend operator fun invoke(request: T, builder: HttpRequestBuilder.() -> Unit = { }): R
}

suspend operator fun <R> ServiceOperator<NoContent, R>.invoke(builder: HttpRequestBuilder.() -> Unit = { }): R =
    invoke(EmptyContent, builder)

inline fun <reified T : Any, reified R> serviceOperator(httpClient: HttpClient, crossinline urlString: (String) -> String) =
    ReadOnlyProperty<Service, ServiceOperator<T, R>> { _, property ->
        serviceOperator(httpClient, urlString(property.name))
    }

inline fun <reified T : Any, reified R> serviceOperator(httpClient: HttpClient, urlString: String) =
    serviceOperator<T, R> { request, builder ->
        httpClient.request(urlString) {
            setBody(request)
            builder()
        }.body()
    }

fun <T, R> serviceOperator(block: suspend (T, HttpRequestBuilder.() -> Unit) -> R) = object : ServiceOperator<T, R> {
    override suspend fun invoke(request: T, builder: HttpRequestBuilder.() -> Unit): R = block(request, builder)
}
