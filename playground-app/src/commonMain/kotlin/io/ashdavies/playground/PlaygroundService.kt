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

@ObsoletePlaygroundApi
public abstract class PlaygroundService(@PublishedApi internal val client: HttpClient) {
    public class Operator<T : Any, R : Any>(private val block: suspend (T, HttpRequestBuilder.() -> Unit) -> R) {
        public suspend operator fun invoke(request: T, builder: HttpRequestBuilder.() -> Unit = {}): R {
            return block(request, builder)
        }
    }
}

@PublishedApi
@ObsoletePlaygroundApi
internal inline fun <reified T : Any, reified R : Any> requesting(
    client: HttpClient, crossinline configure: HttpRequestBuilder.(T) -> Unit
): PlaygroundService.Operator<T, R> = PlaygroundService.Operator { request, builder ->
    client.request {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        configure(request)
        builder()
    }.body()
}

@ObsoletePlaygroundApi
public inline fun <reified T : Any, reified R : Any> PlaygroundService.requesting(
    crossinline configure: HttpRequestBuilder.(T) -> Unit = { },
): ReadOnlyProperty<Any?, PlaygroundService.Operator<T, R>> = ReadOnlyProperty { _, property ->
    requesting(client) {
        path(property.name)
        configure(it)
    }
}

@ObsoletePlaygroundApi
@OptIn(ExperimentalSerializationApi::class)
public inline fun <reified T> HttpRequestBuilder.parameters(value: T) {
    if (value !is EmptyContent) Properties.encodeToMap(value).forEach { (key, value) ->
        if (key != "type" && key.startsWith("value")) parameter(key.substringAfter("value."), value)
    }
}
