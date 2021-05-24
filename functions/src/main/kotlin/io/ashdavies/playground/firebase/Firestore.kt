package io.ashdavies.playground.firebase

import kotlin.js.Promise

external interface Firestore {
    fun <T> collection(path: String): CollectionReference<T>
}

external interface CollectionReference<T> : Query<T>

external interface Query<T> {
    fun doc(documentPath: String): DocumentReference<T>
    fun orderBy(field: String, direction: OrderByDirection): Query<T>
    fun startAt(value: String): Query<T>
    fun limit(limit: Int): Query<T>
    fun get(): Promise<QuerySnapshot<T>>
}

external interface QuerySnapshot<T> {
    val docs: Array<QueryDocumentSnapshot<T>>
}

external interface QueryDocumentSnapshot<T> : DocumentSnapshot {
    fun data(): T
}

external interface DocumentSnapshot {
    val id: String
}

external interface DocumentReference<T> {
    fun get(): Promise<QueryDocumentSnapshot<T>>
    fun set(data: T): Promise<WriteResult>
    fun delete(): Promise<WriteResult>
}

external interface DocumentData

external interface WriteResult

internal typealias OrderByDirection = String
