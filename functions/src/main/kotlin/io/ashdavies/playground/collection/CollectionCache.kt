package io.ashdavies.playground.collection

import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.FirestoreDataConverter
import io.ashdavies.playground.store.Cache
import io.ashdavies.playground.store.Options

internal class CollectionCache<Type : CollectionCache.Type, T : Any>(
    private val collection: CollectionReference<T>,
    private val converter: FirestoreDataConverter<T>,
) : Cache<Type, T> {

    override suspend fun read(key: Type, options: Options): T {
        return collection.read(converter, key.name)
    }

    override suspend fun write(key: Type, value: T) {
        collection.write(key.name, value)
    }

    override suspend fun delete(key: Type) {
        collection.delete(key.name)
    }

    interface Type {
        val name: String
    }
}
