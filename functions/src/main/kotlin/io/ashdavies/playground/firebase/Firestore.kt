package io.ashdavies.playground.firebase

import kotlin.js.Promise

internal external interface Firestore {
    fun collection(path: String): CollectionReference
}

internal interface CollectionReference : Query {
    @JsName("doc") fun doc(documentPath: String): DocumentReference
}

internal interface Query {
    fun orderBy(field: String, direction: OrderByDirection): Query
    fun limit(limit: Int): Query
    fun get(): Promise<QuerySnapshot>
}

internal interface QuerySnapshot {
    val docs: Array<QueryDocumentSnapshot>
}

internal interface QueryDocumentSnapshot : DocumentSnapshot {
    fun data(): DocumentData
}

internal interface DocumentSnapshot {
    val id: String
}

internal interface DocumentReference {
    fun get(): Promise<QueryDocumentSnapshot>
    @JsName("set") fun set(data: DocumentData): Promise<WriteResult>
    fun delete(): Promise<WriteResult>
}

internal interface WriteResult

internal interface DocumentData

internal typealias OrderByDirection = String
