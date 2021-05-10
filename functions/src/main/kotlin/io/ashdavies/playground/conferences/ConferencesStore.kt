package io.ashdavies.playground.conferences

import io.ashdavies.playground.collection.CollectionCache
import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.store.Store

internal suspend fun ConferencesStore(
    collection: CollectionReference,
    token: String,
): ConferencesStore {
    val fetcher = ConferencesFetcher(token)
    val cache = CollectionCache(collection)
    return Store(fetcher, cache)
}

internal typealias ConferencesStore = Store<Unit, List<dynamic>>
