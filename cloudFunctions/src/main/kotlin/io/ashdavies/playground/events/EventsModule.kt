package io.ashdavies.playground.events

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.QuerySnapshot
import io.ashdavies.playground.google.FirebaseScope
import io.ashdavies.playground.google.firestore

private const val COLLECTION_PATH = "events"

fun FirebaseScope.DocumentProvider(path: String = COLLECTION_PATH) = DocumentProvider {
    firestore.collection(path)
}

interface DocumentProvider : (Query.() -> Unit) -> ApiFuture<QuerySnapshot> {
    fun document(childPath: String): DocumentReference
}

fun DocumentProvider(block: () -> CollectionReference) = object : DocumentProvider {
    private val reference: CollectionReference by lazy(LazyThreadSafetyMode.NONE) { block() }
    override fun document(childPath: String): DocumentReference = reference.document(childPath)
    override fun invoke(block: Query.() -> Unit): ApiFuture<QuerySnapshot> = reference
        .apply(block)
        .get()
}
