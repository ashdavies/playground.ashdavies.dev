package io.ashdavies.playground.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.OutgoingContent.NoContent
import io.ktor.http.contentType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.encodeToMap
import kotlin.properties.ReadOnlyProperty

public interface Service {

    val client: HttpClient

    public interface Operator<Request : Any, Response : Any> {
        public suspend operator fun invoke(
            request: Request, builder: HttpRequestBuilder.() -> Unit = {}
        ): Response
    }

    public companion object {

        @PublishedApi
        internal inline fun <reified Request : Any, reified Response : Any> Operator(
            client: HttpClient,
            crossinline urlString: () -> String,
            crossinline configure: HttpRequestBuilder.(Request) -> Unit
        ): Operator<Request, Response> = Operator { request, builder ->
            client.request(urlString()) {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                configure(request)
                builder()
            }.body()
        }

        @PublishedApi
        internal fun <Request : Any, Response : Any> Operator(
            block: suspend (Request, HttpRequestBuilder.() -> Unit) -> Response
        ) = object : Operator<Request, Response> {
            override suspend fun invoke(
                request: Request, builder: HttpRequestBuilder.() -> Unit
            ): Response = block(request, builder)
        }
    }
}

public fun Service(client: HttpClient): Service = object : Service {
    override val client: HttpClient = client
}

@PublishedApi
internal inline fun <reified Request : Any, reified Response : Any> Service.Companion.Operator(
    crossinline urlString: (String) -> String,
    crossinline configure: HttpRequestBuilder.(Request) -> Unit
): ReadOnlyProperty<Service, Service.Operator<Request, Response>> = ReadOnlyProperty { thisRef, property ->
    Operator(thisRef.client, { urlString(property.name) }, configure)
}

public inline fun <reified Request : Any, reified Response : Any> Service.getting(
    crossinline urlString: (String) -> String = { it }
): ReadOnlyProperty<Service, Service.Operator<Request, Response>> = Service.Operator(urlString) {
    method = HttpMethod.Get
    parameters(it)
}

public inline fun <reified Request : Any, reified Response : Any> Service.posting(
    crossinline urlString: (String) -> String = { it }
): ReadOnlyProperty<Service, Service.Operator<Request, Response>> = Service.Operator(urlString) {
    method = HttpMethod.Post
    setBody(it)
}

public suspend operator fun <Response : Any> Service.Operator<NoContent, Response>.invoke(
    builder: HttpRequestBuilder.() -> Unit = { }
): Response = invoke(EmptyContent, builder)

@PublishedApi
@OptIn(ExperimentalSerializationApi::class)
internal inline fun <reified T> HttpRequestBuilder.parameters(value: T) {
    if (value !is EmptyContent) parameters(Properties.encodeToMap(value))
}

@PublishedApi
internal fun HttpRequestBuilder.parameters(values: Map<String, Any?>) {
    values.forEach { (key, value) -> parameter(key, value) }
}
