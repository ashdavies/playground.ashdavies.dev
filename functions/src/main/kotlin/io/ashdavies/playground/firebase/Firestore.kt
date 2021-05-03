package io.ashdavies.playground.firebase

import kotlin.js.Promise

@JsNonModule
@JsModule("firebase-firestore")
internal external class Firestore {
    fun collection(path: String): CollectionReference<DocumentData>
}

internal external interface CollectionReference<T : DocumentData> : Query<T>

internal external interface Query<T : DocumentData> {
    fun orderBy(field: String, direction: OrderByDirection): Query<T>
    fun limit(limit: Int): Query<T>
    fun get(): Promise<QuerySnapshot<T>>
}

internal external interface QuerySnapshot<T : DocumentData> {
    val docs: Array<QueryDocumentSnapshot<T>>
}

internal external interface QueryDocumentSnapshot<T : DocumentData> {
    fun data(): T
}

internal external interface DocumentData

internal typealias OrderByDirection = String