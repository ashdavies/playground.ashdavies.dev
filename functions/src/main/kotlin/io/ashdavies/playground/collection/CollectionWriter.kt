package io.ashdavies.playground.collection

import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.DocumentData

internal class CollectionWriter<T : DocumentData>(
    private val collection: CollectionReference<T>,
    private val identifier: (T) -> String,
) : CacheWriter<T> {

    override suspend fun calculate(
        oldItems: Collection<T>,
        newItems: Collection<T>
    ) {
        for (item in newItems - oldItems) collection.write(identifier(item), item)
        for (item in oldItems - newItems) collection.delete(identifier(item))
    }
}

internal interface CacheWriter<T : DocumentData> {

    suspend fun calculate(
        oldItems: Collection<T>,
        newItems: Collection<T>,
    )
}
