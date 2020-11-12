package io.ashdavies.playground.conferences

import android.content.Context
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import io.ashdavies.playground.Graph
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.database.ConferencesQueries
import io.ashdavies.playground.database.DatabaseFactory
import io.ashdavies.playground.database.DriverFactory
import io.ashdavies.playground.network.ConferencesService
import io.ashdavies.playground.network.httpClient

val Graph<Context>.conferencesService: ConferencesService
    get() = ConferencesService(httpClient)

private val Graph<Context>.conferencesQueries: ConferencesQueries
    get() = DriverFactory(seed.applicationContext)
        .let(::DatabaseFactory)
        .create()
        .conferencesQueries

val Graph<Context>.conferencesStore: Store<Any, List<Conference>>
    get() = StoreBuilder.from(
        sourceOfTruth = ConferencesSourceOfTruth(conferencesQueries),
        fetcher = ConferencesFetcher(conferencesService, ConferencesMapper()),
    ).build()
