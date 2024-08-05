package io.ashdavies.cloud

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.QuerySnapshot
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public interface QueryBuilder {
    public var orderByAscending: String
    public var orderByDescending: String
    public var startAt: Any
    public var limit: Int
}

internal data class FirestoreQueryBuilder(private var query: Query) : QueryBuilder {

    override var orderByAscending: String by setValue {
        query = query.orderBy(it, Query.Direction.ASCENDING)
    }

    override var orderByDescending: String by setValue {
        query = query.orderBy(it, Query.Direction.DESCENDING)
    }

    override var startAt: Any by setValue {
        query = query.startAt(it)
    }

    override var limit: Int by setValue {
        query = query.limit(it)
    }

    fun get(): ApiFuture<QuerySnapshot> = query.get()
}

private inline fun <T> setValue(crossinline block: (T) -> Unit) = WriteOnlyProperty<Any?, T> { _, _, value ->
    block(value)
}

private fun interface WriteOnlyProperty<in T, V> : ReadWriteProperty<T, V> {
    override fun getValue(thisRef: T, property: KProperty<*>): V = throw UnsupportedOperationException()
}
