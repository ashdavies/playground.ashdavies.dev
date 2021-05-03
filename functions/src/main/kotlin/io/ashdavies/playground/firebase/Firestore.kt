package io.ashdavies.playground.firebase

import kotlin.js.Promise

@JsNonModule
@JsModule("firebase-firestore")
internal external class Firestore {
    fun collection(path: String): CollectionReference<DocumentData>
}

internal interface CollectionReference<T : DocumentData> : Query<T> {
    fun doc(documentPath: String): DocumentReference<T>
}

internal interface Query<T : DocumentData> {
    fun orderBy(field: String, direction: OrderByDirection): Query<T>
    fun limit(limit: Int): Query<T>
    fun get(): Promise<QuerySnapshot<T>>
}

internal interface QuerySnapshot<T : DocumentData> {
    val docs: Array<QueryDocumentSnapshot<T>>
}

internal interface QueryDocumentSnapshot<T : DocumentData> : DocumentSnapshot<T> {
    fun data(): T
}

internal interface DocumentSnapshot<T> {
    val id: String
}

internal interface DocumentReference<T> {
    fun set(data: T): Promise<WriteResult>
    fun delete(): Promise<WriteResult>
}

internal interface WriteResult

internal interface DocumentData

internal typealias OrderByDirection = String