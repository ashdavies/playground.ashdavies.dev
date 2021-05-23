package io.ashdavies.playground.collection

import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.Query
import io.ashdavies.playground.firebase.QueryDocumentSnapshot
import io.ashdavies.playground.store.Cache
import io.ashdavies.playground.store.Options
import io.ashdavies.playground.store.Options.Limit
import kotlinx.coroutines.await

internal class CollectionCache<T>(
    private val collection: CollectionReference<T>,
    private val identifier: (T) -> String,
) : Cache<Unit, List<T>> {

    override suspend fun read(key: Unit, options: Options): List<T>? {
        val documents: Array<QueryDocumentSnapshot<T>> = collection
            .orderBy(options.orderBy)
            .startAt(options.startAt)
            .limit(options.limit)
            .get()
            .await()
            .docs

        return when (documents.isNotEmpty()) {
            true -> documents.map { it.data() }
            false -> null
        }
    }

    override suspend fun write(key: Unit, value: List<T>) {
        val writer = CollectionWriter(collection, identifier)
        val options = Options(limit = Limit.Unlimited)

        writer.calculate(
            oldItems = read(key, options) ?: emptyList(),
            newItems = value
        )
    }

    override suspend fun delete(key: Unit) {
        throw UnsupportedOperationException()
    }

    override suspend fun clear() {
        throw UnsupportedOperationException()
    }
}

private fun <T> Query<T>.orderBy(value: String?): Query<T> {
    return if (value != null) orderBy(value, "desc") else this
}

private fun <T> Query<T>.startAt(value: String?): Query<T> {
    return if (value != null) startAt(value) else this
}

private fun <T> Query<T>.limit(limit: Limit): Query<T> {
    return if (limit is Limit.Limited) limit(limit.value) else this
}
