package io.ashdavies.playground.events

import com.google.cloud.firestore.CollectionReference
import io.ashdavies.playground.google.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal fun interface CollectionWriter<T : Any> {
    suspend operator fun invoke(oldValue: Collection<T>, newValue: Collection<T>)
}

internal fun <T : Any> CollectionWriter(reference: CollectionReference, identifier: (T) -> String) =
    CollectionWriter<T> { oldValue, newValue ->
        val queue = OperationQueue(
            oldValue = oldValue.associateBy(identifier),
            newValue = newValue.associateBy(identifier)
        )

        runBlocking {
            for (operation in queue) {
                launch { operation(reference) }.join()
            }
        }
    }

@OptIn(ExperimentalStdlibApi::class)
private fun <T : Any> OperationQueue(oldValue: Map<String, T>, newValue: Map<String, T>) = buildList {
    for ((childPath: String, value: T) in newValue - oldValue.keys) {
        add(WriteOperation(childPath, value))
    }

    for ((childPath: String, _: T) in oldValue - newValue.keys) {
        add(DeleteOperation(childPath))
    }
}

private fun interface CollectionOperation<T> {
    suspend operator fun invoke(reference: CollectionReference)
}

// TODO Ignore nulls
private fun <T : Any> WriteOperation(childPath: String, value: T) = CollectionOperation<T> { reference ->
    reference
        .document(childPath)
        .set(value)
        .await()
}

private fun <T : Any> DeleteOperation(childPath: String) = CollectionOperation<T> { reference ->
    reference
        .document(childPath)
        .delete()
        .await()
}
