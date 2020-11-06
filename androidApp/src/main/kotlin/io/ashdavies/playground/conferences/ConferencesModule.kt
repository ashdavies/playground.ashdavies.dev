package io.ashdavies.playground.conferences

import android.content.Context
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.ashdavies.playground.Graph
import io.ashdavies.playground.database.DatabaseFactory
import io.ashdavies.playground.database.DriverFactory
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferencesQueries
import io.ashdavies.playground.network.LocalDateModule
import io.ashdavies.playground.network.requireContent
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone.Companion.UTC
import kotlinx.datetime.toLocalDateTime

private val Graph<*>.objectMapper: ObjectMapper
    get() = ObjectMapper(YAMLFactory()).apply {
        registerModules(LocalDateModule(), KotlinModule())
    }

private val Graph<Context>.conferencesService: GitHubService<String, ConferenceYaml>
    get() = GitConferencesService(seed.filesDir, "conferences")

private val Graph<Context>.conferencesQueries: ConferencesQueries
    get() = DriverFactory(seed.applicationContext)
        .let(::DatabaseFactory)
        .create()
        .conferencesQueries

private val Graph<Context>.conferenceMapper: (ConferenceYaml) -> Conference
    get() = ConferenceMapper()

val Graph<Context>.conferencesStore: Store<Any, List<Conference>>
    get() = StoreBuilder.from(
        fetcher = Fetcher.of {
            val currentYear: Int = Clock.System.now()
                .toLocalDateTime(UTC)
                .year - 1

            conferencesService
                .getConferences(objectMapper)
                .map { conferenceMapper(it) }
                .filter { it.dateStart.year > currentYear }
                .sortedByDescending { it.dateStart }
        },
        sourceOfTruth = ConferencesSourceOfTruth(conferencesQueries),
    ).build()

private suspend fun GitHubService<String, ConferenceYaml>.getConferences(
    mapper: ObjectMapper
): List<ConferenceYaml> = getAll { mapper.readValue(it.requireContent()) }
