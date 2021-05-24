package io.ashdavies.playground.service

import io.ashdavies.playground.conferences.ConferencesStore
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import io.ashdavies.playground.express.error
import io.ashdavies.playground.firebase.Admin
import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.Config
import io.ashdavies.playground.firebase.Functions
import io.ashdavies.playground.store.Options
import io.ashdavies.playground.store.Options.Limit.Companion.Default
import io.ashdavies.playground.store.Options.Limit.Limited
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic

private const val CONFERENCES = "conferences"

private val Admin.conferences: CollectionReference<Conference>
    get() = firestore().collection(CONFERENCES)

@OptIn(ExperimentalSerializationApi::class)
internal val ConferencesService: (Request, Response<List<Conference>>) -> Unit =
    coroutineService { req, res ->
        val config: Config = Functions.config()
        val arguments = Json.decodeFromDynamic(Arguments.serializer(), req.query)
        val store = ConferencesStore(Admin.conferences, config.github.key)

        store(Unit, arguments.toOptions()).fold(
            onFailure = { res.error(500, it.message) },
            onSuccess = { res.send(it) }
        )
    }

@Serializable
private data class Arguments(
    val refresh: String? = null,
    val orderBy: String? = null,
    val startAt: String? = null,
    val limit: String? = null,
)

private fun Arguments.toOptions() = Options(
    limit = if (limit != null) Limited(limit.toInt()) else Default,
    refresh = refresh.toBoolean(),
    orderBy = orderBy,
    startAt = startAt,
)
