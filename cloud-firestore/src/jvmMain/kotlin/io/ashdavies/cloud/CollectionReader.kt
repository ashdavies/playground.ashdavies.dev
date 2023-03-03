package io.ashdavies.cloud

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties.Default.decodeFromMap

public fun interface CollectionReader<T : Any> {
    public suspend operator fun invoke(deserializer: DeserializationStrategy<T>): List<T>
}

@OptIn(ExperimentalSerializationApi::class)
@Suppress("NO_EXPLICIT_RETURN_TYPE_IN_API_MODE_WARNING")
public fun <T : Any> CollectionReader(provider: DocumentProvider, request: CollectionQuery) = CollectionReader<T> {
    val reference = provider.invoke {
        orderBy = request.orderBy

        if (request.limit > 0) {
            limit = request.limit
        }

        request.startAt?.let {
            startAt = it
        }
    }.await()

    reference.map { snapshot ->
        decodeFromMap(it, snapshot.data)
    }
}
