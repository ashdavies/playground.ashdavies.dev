package io.ashdavies.playground.collection

import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.DocumentData
import io.ashdavies.playground.firebase.Query
import io.ashdavies.playground.store.Cache
import io.ashdavies.playground.store.Options
import io.ashdavies.playground.store.Options.Limit.Limited
import io.ashdavies.playground.store.Options.Limit.Unlimited
import kotlinx.coroutines.await

internal class CollectionCache(
    private val collection: CollectionReference
) : Cache<Unit, List<DocumentData>> {

    override suspend fun read(key: Unit, options: Options): List<DocumentData> {
        return collection
            .orderBy("dateEnd", "desc")
            .startAt(options.startAt)
            .limit(options.limit)
            .get()
            .await()
            .docs
            .map { it.data() }
    }

    override suspend fun write(key: Unit, value: List<DocumentData>) {
        val writer = CollectionWriter(collection) {
            it.asDynamic().id as String
        }

        val options = Options(false, null, Unlimited)
        writer.calculate(read(key, options), value)
    }

    override suspend fun delete(key: Unit) {
        throw UnsupportedOperationException()
    }

    override suspend fun clear() {
        throw UnsupportedOperationException()
    }
}

private fun Query.startAt(value: String?): Query {
    return if (value != null) startAt(value) else this
}

private fun Query.limit(limit: Options.Limit): Query {
    return if (limit is Limited) limit(limit.value) else this
}
