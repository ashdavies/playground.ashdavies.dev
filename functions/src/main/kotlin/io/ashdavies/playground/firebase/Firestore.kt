package io.ashdavies.playground.firebase

import kotlinx.coroutines.await
import kotlin.js.Promise

external interface Firestore {
    fun <T : DocumentData> collection(path: String): CollectionReference<T>
}

external interface CollectionReference<T : DocumentData> : Query<T>

external interface Query<T : DocumentData> {
    fun doc(documentPath: String): DocumentReference<T>
    fun orderBy(field: String, direction: OrderByDirection): Query<T>
    fun startAt(value: String): Query<T>
    fun limit(limit: Int): Query<T>
    fun get(): Promise<QuerySnapshot<T>>
}

external interface QuerySnapshot<T : DocumentData> {
    val docs: Array<QueryDocumentSnapshot<T>>
}

external interface QueryDocumentSnapshot<T : DocumentData> : DocumentSnapshot {
    fun data(): T
}

external interface DocumentSnapshot {
    val id: String
}

external interface DocumentReference<T : DocumentData> {
    fun get(): Promise<QueryDocumentSnapshot<T>>
    fun set(data: DocumentData): Promise<WriteResult>
    fun delete(): Promise<WriteResult>
}

external interface WriteResult

external interface DocumentData

internal typealias OrderByDirection = String

internal suspend fun <T : DocumentData> Query<T>.set(documentPath: String, data: T): WriteResult =
    doc(documentPath)
        .set(data)
        .await()

internal suspend fun <T : DocumentData> Query<T>.delete(documentPath: String): WriteResult =
    doc(documentPath)
        .delete()
        .await()
