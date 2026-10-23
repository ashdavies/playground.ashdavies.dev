package dev.ashdavies.cloud.google

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureToListenableFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.QuerySnapshot
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.guava.await

internal interface FirestoreWriter : AutoCloseable {

    suspend fun getWhereEqualTo(field: String, value: String): Map<String, FirestoreDocument>

    suspend fun set(childPath: String, fields: Map<String, Any>)

    suspend fun delete(childPath: String)

    fun interface Provider {

        operator fun invoke(path: String): FirestoreWriter
    }
}

internal class FirestoreBulkWriter(
    private val firestore: Firestore,
    path: String,
) : FirestoreWriter {

    private val collection = firestore.collection(path)
    private val writer = firestore.bulkWriter()

    override suspend fun getWhereEqualTo(field: String, value: String): Map<String, FirestoreDocument> {
        return collection
            .whereEqualTo(field, value)
            .getAndAwait()
            .associate { it.id to it.data }
    }

    override suspend fun set(childPath: String, fields: Map<String, Any>) {
        println("=== FirestoreBulkWriter.set($childPath, $fields) ===")
        writer.set(collection.document(childPath), fields)
    }

    override suspend fun delete(childPath: String) {
        writer.delete(collection.document(childPath))
    }

    override fun close() {
        writer.close()
    }

    @Inject
    @ContributesBinding(AppScope::class)
    class Provider(private val firebaseApp: FirebaseApp) : FirestoreWriter.Provider {
        override fun invoke(path: String): FirestoreWriter = FirestoreBulkWriter(
            firestore = FirestoreClient.getFirestore(firebaseApp),
            path = path,
        )
    }
}

internal suspend fun <T> ApiFuture<T>.await(): T {
    return ApiFutureToListenableFuture(this).await()
}

internal suspend fun Query.getAndAwait(): QuerySnapshot {
    return get().await()
}
