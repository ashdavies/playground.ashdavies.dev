package io.ashdavies.playground.events

import io.ashdavies.playground.collection.CollectionListCache
import io.ashdavies.playground.configuration.CachePolicy
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.store.Store

internal suspend fun EventsStore(
    collection: CollectionReference<Event>,
    token: String,
): EventsStore = Store(
    cache = CollectionListCache(collection, CachePolicy()) { it.id },
    fetcher = EventsFetcher(GitHubService(token)),
)

internal typealias EventsStore = Store<Unit, List<Event>>
