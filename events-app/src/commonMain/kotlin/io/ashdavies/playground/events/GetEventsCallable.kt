package io.ashdavies.playground.events

import io.ashdavies.http.DefaultHttpClient
import io.ashdavies.http.UnaryCallable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import io.ashdavies.http.common.models.Event as ApiEvent

private const val NETWORK_PAGE_SIZE = 100
private const val PLAYGROUND_API_HOST = "playground.ashdavies.dev"

@Serializable
internal data class GetEventsRequest(
    val startAt: String? = null,
    val limit: Int = NETWORK_PAGE_SIZE,
)

internal typealias GetEventsResponse = List<ApiEvent>

internal class GetEventsCallable(
    httpClient: HttpClient = DefaultHttpClient(),
) : UnaryCallable<GetEventsRequest, GetEventsResponse> {

    private val httpClient = httpClient.config {
        install(DefaultRequest) {
            host = PLAYGROUND_API_HOST
        }

        install(HttpCallValidator) {
            handleResponseExceptionWithRequest { exception, _ ->
                if (exception is ClientRequestException) {
                    throw exception.response.body<GetEventsError>()
                }
            }
        }

        expectSuccess = true
    }

    override suspend fun invoke(request: GetEventsRequest): GetEventsResponse {
        val queryAsString = buildList {
            if (request.startAt != null) add("startAt=${request.startAt}")
            add("limit=${request.limit}")
        }.joinToString("&")

        return httpClient
            .get("events?$queryAsString")
            .body()
    }
}

@Serializable
public data class GetEventsError(
    override val message: String,
    val code: Int,
) : Throwable()
