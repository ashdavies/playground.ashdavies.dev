package io.ashdavies.playground.google

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.WriteResult

suspend fun CollectionReference.delete(childPath: String): WriteResult = document(childPath)
    .delete()
    .await()

suspend fun <T : Any> CollectionReference.write(childPath: String, value: T): WriteResult = document(childPath)
    .set(value)
    .await()
