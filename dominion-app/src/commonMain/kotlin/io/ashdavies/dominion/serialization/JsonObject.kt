package io.ashdavies.dominion.serialization

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal fun JsonElement.getContent(key: String): String =
    jsonObject.getValue(key).jsonPrimitive.content

internal inline fun <reified T : JsonElement> JsonElement.getOrThrow(vararg keys: String): T =
    keys.fold(this) { acc, key -> acc.getOrThrow(key) } as? T ?: error(simpleName<T>())

internal inline fun <reified T : JsonElement> JsonElement.getOrThrow(key: String): T =
    jsonObject.getValue(key) as? T ?: error(simpleName<T>())

internal inline fun <reified T> simpleName(): String =
    T::class.simpleName ?: error(T::class)
