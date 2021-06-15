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
import kotlinx.coroutines.runBlocking

private val Graph<Context>.conferencesService: ConferencesService
    get() = ConferencesService(httpClient)

private fun Graph<Context>.ConferencesQueries(): ConferencesQueries = runBlocking {
    DriverFactory(seed.applicationContext)
        .let(::DatabaseFactory)
        .create()
        .conferencesQueries
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
internal fun Graph<Context>.ConferencesStore(): ConferencesStore =
    StoreBuilder.from(
        sourceOfTruth = ConferencesSourceOfTruth(ConferencesQueries()),
        fetcher = Fetcher.of { conferencesService.get() },
    ).build()

internal typealias ConferencesStore = Store<Unit, List<Conference>>