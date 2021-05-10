package io.ashdavies.playground.service

import io.ashdavies.playground.conferences.ConferencesStore
import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import io.ashdavies.playground.firebase.Admin
import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.store.Options
import io.ashdavies.playground.store.Options.Limit.Limited
import io.ashdavies.playground.store.Options.Limit.Unlimited
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic

private const val CONFERENCES = "conferences"

private val Admin.conferences: CollectionReference
    get() = firestore().collection(CONFERENCES)

@OptIn(ExperimentalSerializationApi::class)
internal val ConferencesService: (Request, Response<dynamic>) -> Unit =
    coroutineService { req, res ->
        val arguments = Json.decodeFromDynamic(Arguments.serializer(), req.query)
        val collection: CollectionReference = Admin.conferences
        val store = ConferencesStore(collection, arguments.token)
        res.send(store(Unit, arguments.toOptions()))
    }

@Serializable
private data class Arguments(
    val refresh: Boolean = false,
    val limit: String? = null,
    val token: String,
)

private fun Arguments.toOptions() = Options(
    limit = if (limit != null) Limited(limit.toInt()) else Unlimited,
    refresh = refresh,
)
