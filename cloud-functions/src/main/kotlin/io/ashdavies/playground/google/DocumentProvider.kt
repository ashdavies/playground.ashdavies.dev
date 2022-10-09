package io.ashdavies.playground.google

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.QuerySnapshot

public interface DocumentProvider : (QueryBuilder.() -> Unit) -> ApiFuture<QuerySnapshot> {
    public fun document(childPath: String): DocumentReference
}

public fun DocumentProvider(block: () -> CollectionReference): DocumentProvider = object : DocumentProvider {
    private val reference: CollectionReference by lazy(LazyThreadSafetyMode.NONE) { block() }
    override fun document(childPath: String): DocumentReference = reference.document(childPath)
    override fun invoke(builder: QueryBuilder.() -> Unit): ApiFuture<QuerySnapshot> = FirestoreQueryBuilder(reference)
        .apply(builder)
        .get()
}
