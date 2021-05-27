package io.ashdavies.playground.service

import io.ashdavies.playground.conferences.ConferencesStore
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import io.ashdavies.playground.express.error
import io.ashdavies.playground.firebase.Admin
import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.EnvironmentConfig
import io.ashdavies.playground.firebase.Functions
import io.ashdavies.playground.store.Options
import io.ashdavies.playground.store.Options.Limit.Companion.Default
import io.ashdavies.playground.store.Options.Limit.Limited
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic

private const val CONFERENCES = "conferences"
private const val DATE_START = "dateStart"

private val Admin.conferences: CollectionReference<Conference>
    get() = firestore().collection(CONFERENCES)

@OptIn(ExperimentalSerializationApi::class)
internal fun ConferencesService(): ConferencesService = coroutineService { req, res ->
    val environment: EnvironmentConfig = Functions.config()
    val arguments = Json.decodeFromDynamic(Arguments.serializer(), req.query)
    val store = ConferencesStore(Admin.conferences, environment.github.key)

    store(Unit, arguments.toOptions()).fold(
        onFailure = { res.error(500, it.message) },
        onSuccess = { res.send(it) }
    )
}

internal typealias ConferencesService = (Request, Response<List<Conference>>) -> Unit

@Serializable
private data class Arguments(
    val refresh: String? = null,
    val orderBy: String? = null,
    val startAt: String? = null,
    val limit: String? = null,
)

private fun Arguments.toOptions() = Options(
    limit = if (limit != null) Limited(limit.toInt()) else Default,
    orderBy = orderBy ?: DATE_START,
    refresh = refresh.toBoolean(),
    startAt = startAt,
)
