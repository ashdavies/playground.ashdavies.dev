package io.ashdavies.playground.conferences

import android.content.Context
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import io.ashdavies.playground.database
import io.ashdavies.playground.network.Conference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

private const val CONFERENCES_DATABASE = "Conferences.db"
private const val GITHUB_BASE_URL = "https://api.github.com/repos/AndroidStudyGroup/conferences"

private val moshiConverterFactory: Converter.Factory
    get() = MoshiConverterFactory.create()

private val retrofit: Retrofit
    get() = Retrofit.Builder()
        .baseUrl(GITHUB_BASE_URL)
        .addConverterFactory(moshiConverterFactory)
        .build()

private val conferencesService: ConferencesService
    get() = retrofit.create()

private val conferencesClient: ConferencesClient
    get() = ConferencesClient(conferencesService)

private val conferencesFetcher: Fetcher<String, List<Conference>>
    get() = Fetcher.of { conferencesClient.getAll() }

private val Context.conferencesDatabase: ConferencesDatabase
    get() = applicationContext.database(CONFERENCES_DATABASE)

private val Context.conferencesDao: ConferencesDao
    get() = conferencesDatabase.dao()

private val ConferencesDao.sourceOfTruth: ConferencesSourceOfTruth
    get() = SourceOfTruth.of(
        nonFlowReader = { getAll() },
        writer = { _, it -> insert(it) },
        deleteAll = { deleteAll() },
        delete = null,
    )

private val Context.sourceOfTruth: ConferencesSourceOfTruth
    get() = conferencesDao.sourceOfTruth

@FlowPreview
@ExperimentalCoroutinesApi
private val Context.conferencesStore: ConferencesStore
    get() = StoreBuilder.from(
        sourceOfTruth = sourceOfTruth,
        fetcher = conferencesFetcher
    ).build()

@FlowPreview
@ExperimentalCoroutinesApi
internal val Context.conferencesRepository: ConferencesRepository
    get() = ConferencesRepository(conferencesClient, conferencesDao, conferencesStore)

private val dateFormat: DateFormat
    get() = SimpleDateFormat("yyyy-MM-dd", Locale.UK)

internal val dateParser: DateParser
    get() = DateParser(dateFormat::parse)

internal typealias ConferencesSourceOfTruth =
    SourceOfTruth<String, List<Conference>, List<Conference>>

internal typealias ConferencesStore =
    Store<String, List<Conference>>
