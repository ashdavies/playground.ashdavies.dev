package io.ashdavies.playground.collection

import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.Query
import io.ashdavies.playground.store.Cache
import io.ashdavies.playground.store.Options
import io.ashdavies.playground.store.Options.Limit
import kotlinx.coroutines.await

internal class CollectionCache<T>(
    private val collection: CollectionReference<T>,
    private val identifier: (T) -> String,
) : Cache<Unit, List<T>> {

    override suspend fun read(key: Unit, options: Options): List<T> {
        return collection
            .orderBy("dateEnd", "desc")
            .startAt(options.startAt)
            .limit(options.limit)
            .get()
            .await()
            .docs
            .map { it.data() }
    }

    override suspend fun write(key: Unit, value: List<T>) {
        val writer = CollectionWriter(collection, identifier)
        val options = Options(limit = Limit.Unlimited)
        writer.calculate(read(key, options), value)
    }

    override suspend fun delete(key: Unit) {
        throw UnsupportedOperationException()
    }

    override suspend fun clear() {
        throw UnsupportedOperationException()
    }
}

private fun <T> Query<T>.startAt(value: String?): Query<T> {
    return if (value != null) startAt(value) else this
}

private fun <T> Query<T>.limit(limit: Limit): Query<T> {
    return if (limit is Limit.Limited) limit(limit.value) else this
}
