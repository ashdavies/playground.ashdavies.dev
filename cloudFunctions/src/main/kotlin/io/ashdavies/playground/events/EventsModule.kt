package io.ashdavies.playground.events

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.Query.Direction.DESCENDING
import com.google.cloud.firestore.QuerySnapshot
import io.ashdavies.playground.google.FirebaseScope
import io.ashdavies.playground.google.firestore
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private const val COLLECTION_PATH = "events"

fun FirebaseScope.DocumentProvider(path: String = COLLECTION_PATH) = DocumentProvider {
    firestore.collection(path)
}

interface DocumentProvider : (QueryBuilder.() -> Unit) -> ApiFuture<QuerySnapshot> {
    fun document(childPath: String): DocumentReference
}

fun DocumentProvider(block: () -> CollectionReference) = object : DocumentProvider {
    private val reference: CollectionReference by lazy(LazyThreadSafetyMode.NONE) { block() }
    override fun document(childPath: String): DocumentReference = reference.document(childPath)
    override fun invoke(builder: QueryBuilder.() -> Unit): ApiFuture<QuerySnapshot> = QueryBuilderImpl(reference)
        .apply(builder)
        .get()
}

private data class QueryBuilderImpl(private var query: Query) : QueryBuilder {
    override var orderBy: String by setValue { query = query.orderBy(it, DESCENDING) }
    override var startAt: Any by setValue { query = query.startAt(it) }
    override var limit: Int by setValue { query = query.limit(it) }
    fun get(): ApiFuture<QuerySnapshot> = query.get()
}

private inline fun <T> setValue(crossinline block: (T) -> Unit) = WriteOnlyProperty<Any?, T> { _, _, value ->
    block(value)
}

private fun interface WriteOnlyProperty<in T, V> : ReadWriteProperty<T, V> {
    override fun getValue(thisRef: T, property: KProperty<*>): V = throw UnsupportedOperationException()
}

interface QueryBuilder {
    var orderBy: String
    var startAt: Any
    var limit: Int
}
