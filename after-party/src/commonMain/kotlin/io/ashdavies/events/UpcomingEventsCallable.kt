package io.ashdavies.events

import io.ashdavies.http.UnaryCallable
import io.ashdavies.http.throwClientRequestExceptionAs
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import io.ashdavies.http.common.models.Event as ApiEvent

private const val NETWORK_PAGE_SIZE = 100

@Serializable
internal data class GetEventsRequest(
    val startAt: String? = null,
    val limit: Int = NETWORK_PAGE_SIZE,
)

internal class UpcomingEventsCallable(
    httpClient: HttpClient,
    private val baseUrl: String,
) : UnaryCallable<GetEventsRequest, List<ApiEvent>> {

    private val httpClient = httpClient.config {
        install(HttpCallValidator) {
            throwClientRequestExceptionAs<GetEventsError>()
        }

        expectSuccess = true
    }

    override suspend fun invoke(request: GetEventsRequest): List<ApiEvent> {
        val queryAsString = buildList {
            if (request.startAt != null) add("startAt=${request.startAt}")
            add("limit=${request.limit}")
        }.joinToString("&")

        return httpClient
            .get("$baseUrl/events/upcoming?$queryAsString")
            .body()
    }
}

@Serializable
internal data class GetEventsError(
    override val message: String,
    val code: Int,
) : Throwable()
