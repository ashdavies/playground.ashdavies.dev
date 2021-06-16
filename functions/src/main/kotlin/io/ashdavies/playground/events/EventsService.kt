package io.ashdavies.playground.events

import io.ashdavies.playground.configuration.Environment
import io.ashdavies.playground.core.coroutineService
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import io.ashdavies.playground.firebase.Admin
import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.admin
import io.ashdavies.playground.store.Options
import io.ashdavies.playground.store.Options.Limit.Companion.Default
import io.ashdavies.playground.store.Options.Limit.Limited
import kotlinx.datetime.Clock.System
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic

private const val CONFERENCES = "conferences"
private const val DATE_START = "dateStart"

private val Admin.events: CollectionReference<Event>
    get() = firestore().collection(CONFERENCES)

private val environment: Environment
    get() = Environment()

internal fun EventsService(): EventsService = coroutineService { req, res ->
    val arguments = Json.decodeFromDynamic(Arguments.serializer(), req.query)
    val store = EventsStore(admin.events, environment.getGithubToken())
    val result: Result<List<Event>> = store(Unit, arguments.toOptions())

    res.send(result.getOrThrow())
}

internal typealias EventsService = (Request, Response<List<Event>>) -> Unit

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
    startAt = startAt ?: today(),
)

private fun today(): String = System.now()
    .toLocalDateTime(TimeZone.UTC)
    .toLocalDate()
    .toString()

private fun LocalDateTime.toLocalDate(): LocalDate =
    LocalDate(year, month, dayOfMonth)
