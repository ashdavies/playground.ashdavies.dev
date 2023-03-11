package io.ashdavies.cloud

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

public fun interface CollectionReader<T : Any> {
    public suspend operator fun invoke(
        deserializer: DeserializationStrategy<T>,
        transform: (Map<String, *>) -> Map<String, *>,
    ): List<T>
}

public suspend operator fun <T : Any> CollectionReader<T>.invoke(
    deserializer: DeserializationStrategy<T>,
): List<T> = invoke(deserializer) { it }

@Suppress("NO_EXPLICIT_RETURN_TYPE_IN_API_MODE_WARNING")
public fun <T : Any> CollectionReader(
    provider: DocumentProvider,
    request: CollectionQuery,
) = CollectionReader<T> { deserializer, transform ->
    val reference = provider.invoke {
        orderBy = request.orderBy

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
        val data = snapshot.data
            .let(transform)
            .asJsonElement()

        json.decodeFromJsonElement(
            deserializer = deserializer,
            element = data
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
