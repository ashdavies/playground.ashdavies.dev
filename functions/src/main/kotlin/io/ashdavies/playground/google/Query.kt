package io.ashdavies.playground.google

import com.google.cloud.firestore.Query

suspend inline fun <reified T : Any> Query.readAll(): List<T> = get()
    .await()
    .toObjects(T::class.java)