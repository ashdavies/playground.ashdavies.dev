package io.ashdavies.playground.conferences

import io.ashdavies.playground.collection.CollectionCache
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.store.Store

internal suspend fun ConferencesStore(
    collection: CollectionReference<Conference>,
    token: String,
): ConferencesStore = Store(
    cache = CollectionCache(collection) { it.id.unsafeCast<String>() },
    fetcher = ConferencesFetcher(GitHubService(token)),
)

internal typealias ConferencesStore = Store<Unit, List<Conference>>
