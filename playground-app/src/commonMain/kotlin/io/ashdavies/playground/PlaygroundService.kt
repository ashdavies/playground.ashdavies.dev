package io.ashdavies.playground

import io.ashdavies.http.path
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.encodeToMap
import kotlin.properties.ReadOnlyProperty

@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
public annotation class ObsoletePlaygroundApi

@ObsoletePlaygroundApi
public abstract class PlaygroundService(@PublishedApi internal val client: HttpClient) {
    public fun interface Operator<T, V> : suspend (T, HttpRequestBuilder.() -> Unit) -> V
}

@ObsoletePlaygroundApi
public inline fun <T, reified R> PlaygroundService.requesting(
    noinline configure: HttpRequestBuilder.(T) -> Unit = { }
): ReadOnlyProperty<Any?, PlaygroundService.Operator<T, R>> = ReadOnlyProperty { _, property ->
    PlaygroundService.Operator { request, builder ->
        client.request {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            path(property.name)
            configure(request)
            builder()
        }.body()
    }
}

@ObsoletePlaygroundApi
@OptIn(ExperimentalSerializationApi::class)
public inline fun <reified T> HttpRequestBuilder.parameters(value: T) {
    if (value !is EmptyContent) Properties.encodeToMap(value).forEach { (key, value) ->
        if (key != "type" && key.startsWith("value")) parameter(key.substringAfter("value."), value)
    }
}
