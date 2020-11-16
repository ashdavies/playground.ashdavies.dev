package io.ashdavies.playground.network

interface FirestoreService<T> {

    suspend fun getAll(): FirestoreCollection<T>

    suspend fun get(key: String): FirestoreDocument<T>
}
