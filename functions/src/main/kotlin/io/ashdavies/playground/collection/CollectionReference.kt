package io.ashdavies.playground.collection

import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.DocumentData
import io.ashdavies.playground.firebase.WriteResult
import kotlinx.coroutines.await

internal suspend fun CollectionReference.read(
    documentPath: String
): DocumentData = doc(documentPath)
    .get()
    .await()
    .data()

internal suspend fun CollectionReference.write(
    documentPath: String,
    data: DocumentData,
): WriteResult = doc(documentPath)
    .set(data)
    .await()

internal suspend fun CollectionReference.delete(
    documentPath: String,
): WriteResult = doc(documentPath)
    .delete()
    .await()
