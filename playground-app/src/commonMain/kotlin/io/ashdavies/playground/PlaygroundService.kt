package io.ashdavies.playground

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
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.encodeToMap
import kotlin.properties.ReadOnlyProperty

@ObsoletePlaygroundApi
public interface PlaygroundService {
    public val client: HttpClient

    public interface Operator<Request : Any, Response : Any> {
        public suspend operator fun invoke(
            request: Request,
            builder: HttpRequestBuilder.() -> Unit = {}
        ): Response
    }

    public companion object
}

@PublishedApi
@ObsoletePlaygroundApi
internal inline fun <reified Request : Any, reified Response : Any> PlaygroundService.Companion.Operator(
    client: HttpClient,
    crossinline urlString: () -> String,
    crossinline configure: HttpRequestBuilder.(Request) -> Unit
): PlaygroundService.Operator<Request, Response> = PlaygroundService.Operator { request, builder ->
    client.request(urlString()) {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        configure(request)
        builder()
    }.body()
}

@PublishedApi
@ObsoletePlaygroundApi
internal fun <Request : Any, Response : Any> PlaygroundService.Companion.Operator(
    block: suspend (Request, HttpRequestBuilder.() -> Unit) -> Response
): PlaygroundService.Operator<Request, Response> = object : PlaygroundService.Operator<Request, Response> {
    override suspend fun invoke(
        request: Request,
        builder: HttpRequestBuilder.() -> Unit
    ): Response = block(request, builder)
}

public fun PlaygroundService(client: HttpClient): PlaygroundService = object : PlaygroundService {
    override val client: HttpClient = client
}

@PublishedApi
@ObsoletePlaygroundApi
internal inline fun <reified Request : Any, reified Response : Any> PlaygroundService.Companion.Operator(
    crossinline urlString: (String) -> String,
    crossinline configure: HttpRequestBuilder.(Request) -> Unit
): ReadOnlyProperty<PlaygroundService, PlaygroundService.Operator<Request, Response>> {
    return ReadOnlyProperty { thisRef, property ->
        PlaygroundService.Operator(
            urlString = { urlString(property.name) },
            client = thisRef.client,
            configure = configure
        )
    }
}

@ObsoletePlaygroundApi
public inline fun <reified Request : Any, reified Response : Any> PlaygroundService.getting(
    crossinline urlString: (String) -> String = { it }
): ReadOnlyProperty<PlaygroundService, PlaygroundService.Operator<Request, Response>> {
    return PlaygroundService.Operator(urlString) {
        method = HttpMethod.Get
        parameters(it)
    }
}

@ObsoletePlaygroundApi
public inline fun <reified Request : Any, reified Response : Any> PlaygroundService.posting(
    crossinline urlString: (String) -> String = { it }
): ReadOnlyProperty<PlaygroundService, PlaygroundService.Operator<Request, Response>> {
    return PlaygroundService.Operator(urlString) {
        method = HttpMethod.Post
        setBody(it)
    }
}

@ObsoletePlaygroundApi
public suspend operator fun <Response : Any> PlaygroundService.Operator<NoContent, Response>.invoke(
    builder: HttpRequestBuilder.() -> Unit = { }
): Response = invoke(EmptyContent, builder)

@PublishedApi
@ObsoletePlaygroundApi
@OptIn(ExperimentalSerializationApi::class)
internal inline fun <reified T> HttpRequestBuilder.parameters(value: T) {
    if (value !is EmptyContent) parameters(Properties.encodeToMap(value))
}

@PublishedApi
@ObsoletePlaygroundApi
internal fun HttpRequestBuilder.parameters(values: Map<String, Any?>) {
    values.forEach { (key, value) ->
        // TODO Figure out parameter encoding properly
        if (key != "type" && key.startsWith("value")) parameter(key.substringAfter("value."), value)
    }
}
