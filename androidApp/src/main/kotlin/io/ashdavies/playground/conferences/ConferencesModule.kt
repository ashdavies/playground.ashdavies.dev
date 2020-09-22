package io.ashdavies.playground.conferences

import android.content.Context
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ashdavies.playground.Database
import io.ashdavies.playground.ktx.insertOrReplaceAll
import io.ashdavies.playground.ktx.selectAllAsFlowList
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferencesQueries
import io.ashdavies.playground.util.DateParser
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

private const val CONFERENCES_DATABASE = "Conferences.db"
private const val GITHUB_BASE_URL = "https://api.github.com/repos/AndroidStudyGroup/conferences"

private val mediaType: MediaType
    get() = "application/json".toMediaType()

private val kotlinJsonConverterFactory: Converter.Factory
    get() = Json.asConverterFactory(mediaType)

private val retrofit: Retrofit
    get() = Retrofit.Builder()
        .baseUrl(GITHUB_BASE_URL)
        .addConverterFactory(kotlinJsonConverterFactory)
        .client(OkHttpClient())
        .build()

private val conferencesService: ConferencesService
    get() = retrofit.create()

private val conferencesClient: ConferencesClient
    get() = ConferencesClient(conferencesService)

private val conferencesFetcher: Fetcher<String, List<Conference>>
    get() = Fetcher.of { conferencesClient.getAll() }

private val Context.sqlDriver: SqlDriver
    get() = AndroidSqliteDriver(Database.Schema, applicationContext, CONFERENCES_DATABASE)

private val Context.database: Database
    get() = Database(sqlDriver)

private val Context.conferenceQueries: ConferencesQueries
    get() = database.conferencesQueries

private val ConferencesQueries.sourceOfTruth: ConferencesSourceOfTruth
    get() = SourceOfTruth.of(
        reader = { selectAllAsFlowList() },
        writer = { _, it -> insertOrReplaceAll(it) },
        deleteAll = { deleteAll() },
        delete = { deleteByName(it) },
    )

private val Context.sourceOfTruth: ConferencesSourceOfTruth
    get() = conferenceQueries.sourceOfTruth

private val Context.conferencesStore: ConferencesStore
    get() = StoreBuilder.from(
        sourceOfTruth = sourceOfTruth,
        fetcher = conferencesFetcher
    ).build()

internal val Context.conferencesRepository: ConferencesRepository
    get() = ConferencesRepository(conferencesClient, conferenceQueries, conferencesStore)

private val dateFormat: DateFormat
    get() = SimpleDateFormat("yyyy-MM-dd", Locale.UK)

internal val dateParser: DateParser
    get() = DateParser {
        dateFormat
            .parse(it)
            .time
    }

internal typealias ConferencesSourceOfTruth =
    SourceOfTruth<String, List<Conference>, List<Conference>>

internal typealias ConferencesStore =
    Store<String, List<Conference>>
