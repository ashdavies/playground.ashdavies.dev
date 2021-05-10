package io.ashdavies.playground.collection

import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.DocumentData
import io.ashdavies.playground.firebase.WriteResult
import kotlinx.coroutines.await

internal suspend fun <T : DocumentData> CollectionReference<T>.read(
    documentPath: String
): T = doc(documentPath)
    .get()
    .await()
    .data()

internal suspend fun <T : DocumentData> CollectionReference<T>.write(
    documentPath: String,
    data: T,
): WriteResult = doc(documentPath)
    .set(data)
    .await()

internal suspend fun <T : DocumentData> CollectionReference<T>.delete(
    documentPath: String,
): WriteResult = doc(documentPath)
    .delete()
    .await()
