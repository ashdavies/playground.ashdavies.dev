package io.ashdavies.cloud

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

public fun interface CollectionWriter<T : Any> {
    public suspend operator fun invoke(
        oldValue: Collection<T>,
        newValue: Collection<T>,
    )
}

@Suppress("NO_EXPLICIT_RETURN_TYPE_IN_API_MODE_WARNING")
public suspend fun <T : Any> CollectionWriter(
    provider: DocumentProvider,
    identifier: (T) -> String,
) = CollectionWriter<T> { oldValue, newValue ->
    val queue = OperationQueue(
        oldValue = oldValue.associateBy(identifier),
        newValue = newValue.associateBy(identifier),
    )

    coroutineScope {
        for (operation in queue) {
            launch(Dispatchers.IO) {
                operation(provider)
            }.join()
        }
    }
}

private fun <T : Any> OperationQueue(oldValue: Map<String, T>, newValue: Map<String, T>) = buildList {
    val newEntries: Map<String, T> = log(newValue - oldValue.keys) { "Writing ${it.size} new entries..." }
    for ((childPath: String, value: T) in newEntries) {
        add(WriteOperation(childPath, value))
    }

    val oldEntries: Map<String, T> = log(oldValue - newValue.keys) { "Deleting ${it.size} entries..." }
    for ((childPath: String, _: T) in oldEntries) {
        add(DeleteOperation(childPath))
    }
}

private fun interface CollectionOperation<T> {
    suspend operator fun invoke(provider: DocumentProvider)
}

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

private fun <T> log(value: T, message: (T) -> String): T {
    return value.also { println(message(it)) }
}
