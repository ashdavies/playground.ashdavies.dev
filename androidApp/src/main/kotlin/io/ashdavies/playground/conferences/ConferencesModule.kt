package io.ashdavies.playground.conferences

import android.content.Context
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ashdavies.playground.Graph
import io.ashdavies.playground.database.DriverFactory
import io.ashdavies.playground.database.PlaygroundDatabase
import io.ashdavies.playground.graph
import io.ashdavies.playground.invoke
import io.ashdavies.playground.ktx.readAll
import io.ashdavies.playground.ktx.writeAll
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferenceFactory
import io.ashdavies.playground.network.ConferencesQueries
import java.lang.System.currentTimeMillis

private val Graph<*>.objectMapper: ObjectMapper
    get() = ObjectMapper(YAMLFactory())
        .apply { registerModule(KotlinModule()) }

private val Graph<Context>.conferencesService: ConferencesService
    get() = GitConferencesService(seed.filesDir, "conferences")

private val Graph<Context>.conferencesQueries: ConferencesQueries
    get() = DriverFactory(seed.applicationContext)
        .create()
        .let { PlaygroundDatabase(it) }
        .conferencesQueries

private val Graph<ConferencesQueries>.sourceOfTruth: ConferencesSourceOfTruth
    get() = SourceOfTruth.of(
        reader = { seed.readAll() },
        writer = { _, it -> seed.writeAll(it) },
        deleteAll = { seed.deleteAll() },
        delete = { seed.deleteAll() },
    )

val Graph<Context>.conferencesStore: ConferencesStore
    get() = StoreBuilder.from(
        sourceOfTruth = conferencesQueries.graph { sourceOfTruth },
        fetcher = Fetcher.of { conferencesService.getConferences() },
    ).build()

private suspend fun ConferencesService.getConferences(
    conferenceFactory: ConferenceFactory = ConferenceFactory { currentTimeMillis() }
): List<Conference> = getAll { conferenceFactory(it.name) }

internal typealias ConferencesService =
    GitHubService<String, Conference>

internal typealias ConferencesSourceOfTruth =
    ListingSourceOfTruth<Any, Conference>

internal typealias ConferencesStore =
    Store<Any, List<Conference>>
