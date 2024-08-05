package io.ashdavies.cloud

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.serializer

public fun interface CollectionReader<T : Any> {
    public suspend operator fun invoke(
        deserializer: DeserializationStrategy<T>,
    ): List<T>
}

public suspend inline operator fun <reified T : Any> CollectionReader<T>.invoke(): List<T> {
    return invoke(serializer())
}

public fun <T : Any> CollectionReader(
    provider: DocumentProvider,
    request: CollectionQuery,
): CollectionReader<T> = CollectionReader { deserializer ->
    val reference = provider.invoke {
        orderByAscending = request.orderBy

        request.startAt?.let {
            startAt = it
        }

        if (request.limit > 0) {
            limit = request.limit
        }
    }.await()

    val json = Json {
        ignoreUnknownKeys = true
    }

    reference.map { snapshot ->
        val data = snapshot.data.asJsonElement()

        json.decodeFromJsonElement(
            deserializer = deserializer,
            element = data,
        )
    }
}

private fun Map<*, *>.asJsonElement(): JsonElement = buildJsonObject {
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
