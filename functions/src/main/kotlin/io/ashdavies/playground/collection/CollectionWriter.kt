package io.ashdavies.playground.collection

import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.DocumentData

internal class CollectionWriter(
    private val collection: CollectionReference,
    private val selector: (DocumentData) -> String,
) : CacheWriter {

    override suspend fun calculate(
        oldItems: Collection<DocumentData>,
        newItems: Collection<DocumentData>
    ) {
        (newItems - oldItems).forEach { collection.write(selector(it), it) }
        (oldItems - newItems).forEach { collection.delete(selector(it)) }
    }
}

internal interface CacheWriter {

    suspend fun calculate(
        oldItems: Collection<DocumentData>,
        newItems: Collection<DocumentData>,
    )
}
