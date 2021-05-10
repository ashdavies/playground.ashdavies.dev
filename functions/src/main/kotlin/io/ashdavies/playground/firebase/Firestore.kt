package io.ashdavies.playground.firebase

import kotlinx.coroutines.await
import kotlin.js.Promise

external interface Firestore {
    fun collection(path: String): CollectionReference
}

external interface CollectionReference : Query

external interface Query {
    fun doc(documentPath: String): DocumentReference
    fun orderBy(field: String, direction: OrderByDirection): Query
    fun limit(limit: Int): Query
    fun get(): Promise<QuerySnapshot>
}

external interface QuerySnapshot {
    val docs: Array<QueryDocumentSnapshot>
}

external interface QueryDocumentSnapshot : DocumentSnapshot {
    fun data(): DocumentData
}

external interface DocumentSnapshot {
    val id: String
}

external interface DocumentReference {
    fun get(): Promise<QueryDocumentSnapshot>
    fun set(data: DocumentData): Promise<WriteResult>
    fun delete(): Promise<WriteResult>
}

external interface WriteResult

external interface DocumentData

internal typealias OrderByDirection = String

internal suspend fun Query.set(documentPath: String, data: DocumentData): WriteResult =
    doc(documentPath)
        .set(data)
        .await()

internal suspend fun Query.delete(documentPath: String): WriteResult =
    doc(documentPath)
        .delete()
        .await()
