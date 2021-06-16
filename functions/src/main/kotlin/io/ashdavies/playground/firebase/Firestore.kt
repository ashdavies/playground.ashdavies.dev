package io.ashdavies.playground.firebase

import com.google.api.core.ApiFuture

internal interface Firestore {
    fun <T> collection(path: String): CollectionReference<T>
}

internal interface CollectionReference<T> : Query<T>

internal interface Query<T> {
    fun doc(documentPath: String): DocumentReference<T>
    fun where(fieldPath: String, opStr: WhereFilterOp, value: Any): Query<T>
    fun orderBy(field: String, direction: OrderByDirection): Query<T>
    fun startAt(value: String): Query<T>
    fun limit(limit: Int): Query<T>
    fun get(): ApiFuture<QuerySnapshot<T>>
}

internal interface QuerySnapshot<T> {
    val docs: Array<QueryDocumentSnapshot<T>>
}

internal interface QueryDocumentSnapshot<T> : DocumentSnapshot {
    fun data(): T
}

internal interface DocumentSnapshot {
    val id: String
}

internal interface DocumentReference<T> {
    fun withConverter(converter: FirestoreDataConverter<T>): DocumentReference<T>

    fun get(): ApiFuture<QueryDocumentSnapshot<T>>
    fun set(data: T): ApiFuture<WriteResult>
    fun delete(): ApiFuture<WriteResult>
}

internal interface FirestoreDataConverter<T> {
    fun fromFirestore(snapshot: QueryDocumentSnapshot<T>, options: SnapshotOptions): T
}

internal interface SnapshotOptions {
    val serverTimestamps: String?
}

internal interface WriteResult

typealias OrderByDirection = String

typealias WhereFilterOp = String