package dev.ashdavies.cloud.events

import dev.ashdavies.cloud.google.FirestoreDocument
import dev.ashdavies.cloud.google.FirestoreWriter

internal class TestFirestoreWriter(documents: Map<String, FirestoreDocument> = emptyMap()) : FirestoreWriter {

    val documents: Map<String, FirestoreDocument>
        field = documents.toMutableMap()

    var isClosed: Boolean = false
        private set

    var writes: Int = 0
        private set

    override suspend fun getWhereEqualTo(field: String, value: String): Map<String, FirestoreDocument> {
        if (isClosed) throw IllegalStateException("Writer is already closed")
        return documents.filter { (_, doc) -> doc[field] == value }
    }

    override suspend fun set(childPath: String, fields: Map<String, Any>) {
        if (isClosed) throw IllegalStateException("Writer is already closed")
        documents[childPath] = fields
        writes++
    }

    override suspend fun delete(childPath: String) {
        if (isClosed) throw IllegalStateException("Writer is already closed")
        documents.remove(childPath)
        writes++
    }

    override fun close() {
        if (isClosed) throw IllegalStateException("Writer is already closed")
        isClosed = true
    }
}
