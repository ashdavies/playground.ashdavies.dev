package io.ashdavies.playground.conferences

import android.content.Context
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import io.ashdavies.playground.Graph
import io.ashdavies.playground.database.DriverFactory
import io.ashdavies.playground.database.PlaygroundDatabase
import io.ashdavies.playground.graph
import io.ashdavies.playground.invoke
import io.ashdavies.playground.ktx.readByName
import io.ashdavies.playground.ktx.writeAll
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferencesQueries
import io.ashdavies.playground.network.httpClient

private val Graph<*>.conferencesService: ConferencesService
    get() = ConferencesService(httpClient)

private val Graph<Context>.conferencesQueries: ConferencesQueries
    get() = DriverFactory(seed.applicationContext)
        .create()
        .let { PlaygroundDatabase(it) }
        .conferencesQueries

internal val Graph<Context>.conferencesRepository: ConferencesRepository
    get() = ConferencesRepository(conferencesStore)

private val Graph<ConferencesQueries>.sourceOfTruth: ConferencesSourceOfTruth
    get() = SourceOfTruth.of(
        writer = { k, i -> seed.writeAll(k, i) },
        delete = { seed.deleteByName(it) },
        deleteAll = { seed.deleteAll() },
        reader = { seed.readByName(it) },
    )

val Graph<Context>.conferencesStore: ConferencesStore
    get() = StoreBuilder.from(
        sourceOfTruth = conferencesQueries.graph { sourceOfTruth },
        fetcher = Fetcher.of { conferencesService.getAll() },
    ).build()

internal typealias ConferencesSourceOfTruth =
    ListingSourceOfTruth<String, Conference>

internal typealias ConferencesStore =
    Store<String, List<Conference>>
