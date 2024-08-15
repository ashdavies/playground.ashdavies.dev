package io.ashdavies.cloud

import com.google.cloud.firestore.QuerySnapshot
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement

public inline fun <reified T : Any> Json.decodeFromSnapshot(snapshot: QuerySnapshot): List<T> {
    return snapshot.map { decodeFromJsonElement(it.data.asJsonElement()) }
}

@PublishedApi
internal fun Map<*, *>.asJsonElement(): JsonElement = buildJsonObject {
    forEach { (key, value) ->
        when (value) {
            is Map<*, *> -> put(key as String, value.asJsonElement())
            is Boolean -> put(key as String, JsonPrimitive(value))
            is Number -> put(key as String, JsonPrimitive(value))
            is String -> put(key as String, JsonPrimitive(value))
            null -> put(key as String, JsonNull)
        }
    }
}
