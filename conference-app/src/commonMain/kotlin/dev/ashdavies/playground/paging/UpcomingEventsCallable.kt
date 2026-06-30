package dev.ashdavies.playground.paging

import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.getString
import dev.ashdavies.http.UnaryCallable
import dev.ashdavies.http.common.models.ApiConference
import dev.ashdavies.http.throwClientRequestExceptionAs
import dev.ashdavies.sql.Suspended
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

private const val NETWORK_PAGE_SIZE = 100

internal fun interface UpcomingEventsCallable : UnaryCallable<GetEventsRequest, List<ApiConference>>

private suspend fun RemoteConfig.eventsEndpoint() = getString("events_endpoint")

@Serializable
internal data class GetEventsRequest(
    val startAt: String? = null,
    val limit: Int = NETWORK_PAGE_SIZE,
)

internal fun UpcomingEventsCallable(httpClient: HttpClient, remoteConfig: RemoteConfig): UpcomingEventsCallable {
    val pagedCallable = Suspended { UpcomingEventsCallable(httpClient, remoteConfig.eventsEndpoint()) }
    return UpcomingEventsCallable { pagedCallable()(it) }
}

internal fun UpcomingEventsCallable(httpClient: HttpClient, baseUrl: String): UpcomingEventsCallable {
    val errorHandlingHttpClient = httpClient.config {
        install(HttpCallValidator) { throwClientRequestExceptionAs<GetEventsError>() }
        expectSuccess = true
    }

    return UpcomingEventsCallable { request ->
        val queryAsString = buildList {
            if (request.startAt != null) add("startAt=${request.startAt}")
            add("limit=${request.limit}")
        }.joinToString("&")

        errorHandlingHttpClient
            .get("https://$baseUrl/events/upcoming?$queryAsString")
            .body()
    }
}

@Serializable
internal data class GetEventsError(
    override val message: String,
    val code: Int,
) : Throwable()
