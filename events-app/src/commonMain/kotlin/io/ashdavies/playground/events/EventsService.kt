package io.ashdavies.playground.events

import io.ashdavies.http.common.models.Event
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import io.ashdavies.http.common.models.Event as ApiEvent

private const val PLAYGROUND_API_HOST = "playground.ashdavies.dev"

private const val NETWORK_PAGE_SIZE = 100

internal interface EventsService {
    suspend fun getEvents(
        startAt: String? = null,
        limit: Int = NETWORK_PAGE_SIZE,
    ): List<ApiEvent>
}

internal fun EventsService(client: HttpClient): EventsService = object : EventsService {
    override suspend fun getEvents(startAt: String?, limit: Int): List<Event> {
        val query = buildList {
            if (startAt != null) add("startAt=$startAt")
            add("limit=$limit")
        }

        val response = client.get("https://$PLAYGROUND_API_HOST/events?startAt=${query.joinToString("&")}")
        if (response.status == HttpStatusCode.OK) {
            return response.body()
        }

        throw response.body<ErrorResponse>()
    }
}

@Serializable
internal data class ErrorResponse(
    override val message: String,
    val code: Int,
) : Throwable()
