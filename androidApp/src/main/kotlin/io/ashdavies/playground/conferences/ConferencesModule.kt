package io.ashdavies.playground.conferences

import android.content.Context
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import io.ashdavies.playground.database.DriverFactory
import io.ashdavies.playground.database.PlaygroundDatabase
import io.ashdavies.playground.ktx.insertOrReplaceAll
import io.ashdavies.playground.ktx.selectAllAsFlowList
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferencesQueries

private val conferencesClient: ConferencesClient
    get() = ConferencesClient(ConferencesService())

private val Context.conferencesQueries: ConferencesQueries
    get() = DriverFactory(this)
        .create()
        .let { PlaygroundDatabase(it) }
        .conferencesQueries

private val Context.conferencesStore: ConferencesStore
    get() = StoreBuilder.from(
        fetcher = Fetcher.of { conferencesClient.getAll() },
        sourceOfTruth = conferencesQueries.sourceOfTruth
    ).build()

private val ConferencesQueries.sourceOfTruth: ConferencesSourceOfTruth
    get() = SourceOfTruth.of(
        reader = { selectAllAsFlowList() },
        writer = { _, it -> insertOrReplaceAll(it) },
        deleteAll = { deleteAll() },
        delete = { deleteByName(it) },
    )

internal val Context.conferencesRepository: ConferencesRepository
    get() = ConferencesRepository(
        conferencesClient = conferencesClient,
        conferencesQueries = conferencesQueries,
        conferencesStore = conferencesStore,
    )

internal typealias ConferencesSourceOfTruth =
    SourceOfTruth<String, List<Conference>, List<Conference>>

internal typealias ConferencesStore =
    Store<String, List<Conference>>
