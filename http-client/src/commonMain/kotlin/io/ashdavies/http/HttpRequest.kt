package io.ashdavies.http

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.encodeToMap

@OptIn(ExperimentalSerializationApi::class)
public inline fun <reified T> HttpRequestBuilder.parameter(value: T) {
    Properties.encodeToMap(value).forEach { (key, value) -> parameter(key, value) }
}
