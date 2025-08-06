package dev.ashdavies.cloud

import com.google.cloud.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.collections.iterator
import kotlin.coroutines.CoroutineContext

public fun interface CollectionWriter<T : Any> {
    public suspend operator fun invoke(
        oldValue: Collection<T>,
        newValue: Collection<T>,
    )
}

public fun <T : Any> CollectionWriter(
    reference: CollectionReference,
    identifier: (T) -> String,
    context: CoroutineContext = Dispatchers.IO,
): CollectionWriter<T> = CollectionWriter { oldValue, newValue ->
    val queue = operationQueue(
        oldValue = oldValue.associateBy(identifier),
        newValue = newValue.associateBy(identifier),
    )

    coroutineScope {
        for (operation in queue) {
            launch(context) {
                operation(reference)
            }.join()
        }
    }
}

private fun <T : Any> operationQueue(oldValue: Map<String, T>, newValue: Map<String, T>) = buildList {
    val newEntries: Map<String, T> = log(newValue - oldValue.keys) { "Writing ${it.size} new entries..." }
    for ((childPath: String, value: T) in newEntries) {
        add(writeOperation(childPath, value))
    }

    val oldEntries: Map<String, T> = log(oldValue - newValue.keys) { "Deleting ${it.size} entries..." }
    for ((childPath: String, _: T) in oldEntries) {
        add(deleteOperation(childPath))
    }
}

private fun interface CollectionOperation<T> {
    suspend operator fun invoke(reference: CollectionReference)
}

private fun <T : Any> writeOperation(childPath: String, value: T) = CollectionOperation<T> { reference ->
    reference
        .document(childPath)
        .set(value)
        .await()
}

private fun <T : Any> deleteOperation(childPath: String) = CollectionOperation<T> { reference ->
    reference
        .document(childPath)
        .delete()
        .await()
}

private fun <T> log(value: T, message: (T) -> String): T {
    return value.also { println(message(it)) }
}
