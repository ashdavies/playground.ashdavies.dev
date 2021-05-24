package io.ashdavies.playground.collection

import io.ashdavies.playground.firebase.CollectionReference

internal class CollectionListWriter<T>(
    private val collection: CollectionReference<T>,
    private val identifier: (T) -> String,
) : CacheWriter<T> {

    override suspend fun calculate(
        oldItems: Collection<T>,
        newItems: Collection<T>,
    ) {
        val oldItemsByKey: Map<String, T> = oldItems.associateBy(identifier)
        val newItemsByKey: Map<String, T> = newItems.associateBy(identifier)

        val writeQueue: Map<String, T> = newItemsByKey - oldItemsByKey.keys
        for ((key: String, value: T) in writeQueue) collection.write(key, value)

        val deleteQueue: Map<String, T> = oldItemsByKey - newItemsByKey.keys
        for ((key: String, _: T) in deleteQueue) collection.delete(key)
    }
}

internal interface CacheWriter<T> {

    suspend fun calculate(
        oldItems: Collection<T>,
        newItems: Collection<T>,
    )
}
