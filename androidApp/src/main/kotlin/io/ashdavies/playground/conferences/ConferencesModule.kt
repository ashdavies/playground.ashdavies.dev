package io.ashdavies.playground.conferences

import android.content.Context
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import io.ashdavies.playground.Graph
import io.ashdavies.playground.database.DatabaseFactory
import io.ashdavies.playground.database.DriverFactory
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferenceService
import io.ashdavies.playground.network.ConferencesQueries
import io.ashdavies.playground.network.httpClient


private val Graph<Context>.conferencesService: ConferenceService
    get() = ConferenceService(httpClient)

private val Graph<Context>.conferencesQueries: ConferencesQueries
    get() = DriverFactory(seed.applicationContext)
        .let(::DatabaseFactory)
        .create()
        .conferencesQueries

val Graph<Context>.conferencesStore: Store<Any, List<Conference>>
    get() = StoreBuilder.from(
        sourceOfTruth = ConferencesSourceOfTruth(conferencesQueries),
        fetcher = ConferencesFetcher(conferencesService),
    ).build()
