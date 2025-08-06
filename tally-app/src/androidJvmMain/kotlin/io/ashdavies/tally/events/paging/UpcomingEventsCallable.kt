package io.ashdavies.tally.events.paging

import dev.ashdavies.asg.UpcomingConferencesCallable
import dev.ashdavies.cloud.ApiConferenceFactory
import dev.ashdavies.cloud.Identifier
import dev.ashdavies.http.UnaryCallable
import dev.ashdavies.http.asSequence
import dev.ashdavies.http.common.models.ApiConference
import dev.ashdavies.http.throwClientRequestExceptionAs
import io.ashdavies.config.RemoteConfig
import io.ashdavies.config.getBoolean
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

private const val PLAYGROUND_BASE_URL = "api.ashdavies.dev"
private const val NETWORK_PAGE_SIZE = 100

internal fun interface UpcomingEventsCallable : UnaryCallable<GetEventsRequest, List<ApiConference>>

private suspend fun RemoteConfig.isPagingEnabled() = getBoolean("paging_enabled")

@Serializable
internal data class GetEventsRequest(
    val startAt: String? = null,
    val limit: Int = NETWORK_PAGE_SIZE,
)

internal fun UpcomingEventsCallable(httpClient: HttpClient, remoteConfig: RemoteConfig): UpcomingEventsCallable {
    val pagedCallable by lazy { UpcomingEventsCallable(httpClient, PLAYGROUND_BASE_URL) }
    val asgCallable by lazy { UpcomingConferencesCallable(httpClient) }
    val apiConferenceFactory = ApiConferenceFactory(Identifier())

    return UpcomingEventsCallable { request ->
        when {
            remoteConfig.isPagingEnabled() -> pagedCallable(request)
            else -> asgCallable.asSequence(Unit) { response ->
                response
                    .filter { request.startAt == null || it.dateStart > request.startAt }
                    .take(request.limit)
                    .map(apiConferenceFactory::invoke)
            }
        }
    }
}

internal fun UpcomingEventsCallable(httpClient: HttpClient, baseUrl: String): UpcomingEventsCallable {
    val errorHandlingHttpClient = httpClient.config {
        install(HttpCallValidator) {
            throwClientRequestExceptionAs<GetEventsError>()
        }

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
