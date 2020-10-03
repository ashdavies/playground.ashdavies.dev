package io.ashdavies.playground.conferences

import android.content.Context
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import io.ashdavies.playground.conferences.StoreModule.conferencesStore
import io.ashdavies.playground.database.DriverFactory
import io.ashdavies.playground.database.PlaygroundDatabase
import io.ashdavies.playground.ktx.readByName
import io.ashdavies.playground.ktx.writeAll
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferencesQueries

private val conferencesService: ConferencesService
    get() = ConferencesService()

private val Context.conferencesQueries: ConferencesQueries
    get() = DriverFactory(applicationContext)
        .create()
        .let { PlaygroundDatabase(it) }
        .conferencesQueries

internal val Context.conferencesRepository: ConferencesRepository
    get() = ConferencesRepository(conferencesStore)

internal object StoreModule {

    private val ConferencesQueries.sourceOfTruth: ConferencesSourceOfTruth
        get() = SourceOfTruth.of(
            writer = { k, i -> writeAll(k, i) },
            delete = { deleteByName(it) },
            deleteAll = { deleteAll() },
            reader = { readByName(it) },
        )

    val Context.conferencesStore: ConferencesStore
        get() = StoreBuilder.from(
            fetcher = Fetcher.of { conferencesService.getAll() },
            sourceOfTruth = conferencesQueries.sourceOfTruth,
        ).build()
}

internal typealias ConferencesSourceOfTruth = ListingSourceOfTruth<String, Conference>

internal typealias ConferencesStore = Store<String, List<Conference>>
