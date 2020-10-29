package io.ashdavies.playground.conferences

import android.content.Context
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.ashdavies.playground.Graph
import io.ashdavies.playground.database.DriverFactory
import io.ashdavies.playground.database.PlaygroundDatabase
import io.ashdavies.playground.graph
import io.ashdavies.playground.invoke
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferencesQueries
import io.ashdavies.playground.network.requireContent
import kotlinx.coroutines.flow.Flow

private val Graph<*>.objectMapper: ObjectMapper
    get() = ObjectMapper(YAMLFactory()).apply {
        propertyNamingStrategy = SNAKE_CASE
        registerModule(KotlinModule())
    }

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
        fetcher = Fetcher.of { conferencesService.getConferences(objectMapper) },
        sourceOfTruth = conferencesQueries.graph { sourceOfTruth },
    ).build()

private suspend fun ConferencesService.getConferences(mapper: ObjectMapper): List<Conference> =
    getAll { mapper.readValue(it.requireContent()) }

private fun ConferencesQueries.writeAll(conferences: Iterable<Conference>) =
    conferences.forEach { insertOrReplace(it) }

private fun ConferencesQueries.readAll(): Flow<List<Conference>> =
    selectAll().asFlowList()

private fun <T : Any> Query<T>.asFlowList(): Flow<List<T>> =
    asFlow().mapToList()

internal typealias ConferencesService =
    GitHubService<String, Conference>

internal typealias ConferencesSourceOfTruth =
    ListingSourceOfTruth<Any, Conference>

internal typealias ConferencesStore =
    Store<Any, List<Conference>>
