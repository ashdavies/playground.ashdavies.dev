package io.ashdavies.playground.conferences

import android.content.Context
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import io.ashdavies.playground.Graph
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.database.ConferencesQueries
import io.ashdavies.playground.database.DatabaseFactory
import io.ashdavies.playground.database.DriverFactory
import io.ashdavies.playground.network.ConferencesService
import io.ashdavies.playground.network.httpClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

val Graph<Context>.conferencesService: ConferencesService
    get() = ConferencesService(httpClient)

private suspend fun Graph<Context>.conferencesQueries(): ConferencesQueries =
    DriverFactory(seed.applicationContext)
        .let(::DatabaseFactory)
        .create()
        .conferencesQueries

@FlowPreview
@ExperimentalCoroutinesApi
suspend fun Graph<Context>.conferencesStore(token: String): Store<Unit, List<Conference>> =
    StoreBuilder.from(
        sourceOfTruth = ConferencesSourceOfTruth(conferencesQueries()),
        fetcher = Fetcher.of { conferencesService.get(token) },
    ).build()
