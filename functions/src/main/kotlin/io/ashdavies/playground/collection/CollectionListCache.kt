package io.ashdavies.playground.collection

import io.ashdavies.playground.configuration.CachePolicy
import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.OrderByDirection
import io.ashdavies.playground.firebase.Query
import io.ashdavies.playground.firebase.QueryDocumentSnapshot
import io.ashdavies.playground.firebase.WhereFilterOp
import io.ashdavies.playground.google.await
import io.ashdavies.playground.store.Cache
import io.ashdavies.playground.store.Options
import io.ashdavies.playground.store.Options.Limit

internal class CollectionListCache<T>(
    private val collection: CollectionReference<T>,
    private val cachePolicy: CachePolicy,
    private val identifier: (T) -> String,
) : Cache<Unit, List<T>> {

    override suspend fun read(key: Unit, options: Options): List<T>? {
        if (cachePolicy.isStale(options.refresh)) {
            return null
        }

        val documents: Array<QueryDocumentSnapshot<T>> = collection
            .where(options.orderBy, ">", options.startAt)
            .orderBy(options.orderBy, "asc")
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
        val writer = CollectionListWriter(collection, identifier)
        val options = Options(limit = Limit.Unlimited)

        writer.calculate(
            oldItems = read(key, options) ?: emptyList(),
            newItems = value
        )

        cachePolicy.isUpToDate(true)
    }

    override suspend fun delete(key: Unit) {
        throw UnsupportedOperationException()
    }

    override suspend fun clear() {
        throw UnsupportedOperationException()
    }
}

private fun <T> Query<T>.where(fieldPath: String?, opStr: WhereFilterOp, value: Any?): Query<T> {
    return if (fieldPath != null && value != null) where(fieldPath, opStr, value) else this
}

private fun <T> Query<T>.orderBy(value: String?, direction: OrderByDirection): Query<T> {
    return if (value != null) orderBy(value, direction) else this
}

private fun <T> Query<T>.startAt(value: String?): Query<T> {
    return if (value != null) startAt(value) else this
}

private fun <T> Query<T>.limit(limit: Limit): Query<T> {
    return if (limit is Limit.Limited) limit(limit.value) else this
}
