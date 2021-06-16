package io.ashdavies.playground.collection

import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.FirestoreDataConverter
import io.ashdavies.playground.firebase.WriteResult
import io.ashdavies.playground.google.await

internal suspend fun <T> CollectionReference<T>.read(
    converter: FirestoreDataConverter<T>,
    documentPath: String,
): T = doc(documentPath)
    .withConverter(converter)
    .get()
    .await()
    .data()

internal suspend fun <T> CollectionReference<T>.write(
    documentPath: String,
    data: T,
): WriteResult = doc(documentPath)
    .set(data)
    .await()

internal suspend fun <T> CollectionReference<T>.delete(
    documentPath: String,
): WriteResult = doc(documentPath)
    .delete()
    .await()
