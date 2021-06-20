package io.ashdavies.playground.events

import android.content.Context
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import io.ashdavies.playground.Graph
import io.ashdavies.playground.database.DatabaseFactory
import io.ashdavies.playground.database.DriverFactory
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.database.EventsQueries
import io.ashdavies.playground.network.EventsService
import io.ashdavies.playground.network.httpClient

private val Graph<Context>.eventsService: EventsService
    get() = EventsService(httpClient)

private suspend fun Graph<Context>.EventsQueries(): EventsQueries =
    DriverFactory(seed.applicationContext)
        .let(::DatabaseFactory)
        .create()
        .eventsQueries

internal suspend fun Graph<Context>.EventsStore(): EventsStore =
    StoreBuilder.from(
        sourceOfTruth = EventsSourceOfTruth(EventsQueries()),
        fetcher = Fetcher.of { eventsService.get() },
    ).build()

internal typealias EventsStore = Store<Unit, List<Event>>