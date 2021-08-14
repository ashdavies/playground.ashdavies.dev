package io.ashdavies.playground.events

import com.dropbox.android.external.store4.SourceOfTruth
import com.google.cloud.firestore.CollectionReference
import io.ashdavies.playground.google.delete
import io.ashdavies.playground.google.readAll
import io.ashdavies.playground.google.write
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal abstract class CollectionSourceOfTruth<T : Any>(
    private val collection: CollectionReference,
    private val identifier: (T) -> String,
) : SourceOfTruth<Unit, List<T>, List<T>> {

    override fun reader(key: Unit): Flow<List<T>> = flow {
        emit(nonFlowReader(key))
    }

    abstract suspend fun nonFlowReader(key: Unit): List<T>

    override suspend fun write(key: Unit, value: List<T>) {
        val oldValue: Map<String, T> = nonFlowReader(Unit).associateBy(identifier)
        val newValue: Map<String, T> = value.associateBy(identifier)

        for ((id: String, item: T) in newValue - oldValue.keys) collection.write(id, item)
        for ((id: String, _: T) in oldValue - newValue.keys) collection.delete(id)
    }

    override suspend fun delete(key: Unit) = throw UnsupportedOperationException()

    override suspend fun deleteAll() = throw UnsupportedOperationException()
}
