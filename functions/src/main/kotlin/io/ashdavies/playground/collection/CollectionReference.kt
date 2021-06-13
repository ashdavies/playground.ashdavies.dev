package io.ashdavies.playground.collection

import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.FirestoreDataConverter
import io.ashdavies.playground.firebase.WriteResult
import kotlinx.coroutines.await
import kotlin.js.JSON.parse
import kotlin.js.JSON.stringify

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
    .set(parse(stringify(data)))
    .await()

internal suspend fun <T> CollectionReference<T>.delete(
    documentPath: String,
): WriteResult = doc(documentPath)
    .delete()
    .await()
