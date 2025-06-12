package io.ashdavies.tally.events.paging

import io.ashdavies.asg.AsgConference
import io.ashdavies.asg.UpcomingConferencesCallable
import io.ashdavies.config.RemoteConfig
import io.ashdavies.config.getBoolean
import io.ashdavies.http.UnaryCallable
import io.ashdavies.http.common.models.EventCfp
import io.ashdavies.http.throwClientRequestExceptionAs
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encode
import io.ashdavies.http.common.models.Event as ApiEvent

private const val PLAYGROUND_BASE_URL = "api.ashdavies.dev"
private const val NETWORK_PAGE_SIZE = 100

internal fun interface UpcomingEventsCallable : UnaryCallable<GetEventsRequest, List<ApiEvent>>

private suspend fun RemoteConfig.isPagingEnabled() = getBoolean("paging_enabled")

@Serializable
internal data class GetEventsRequest(
    val startAt: String? = null,
    val limit: Int = NETWORK_PAGE_SIZE,
)

internal fun UpcomingEventsCallable(httpClient: HttpClient, remoteConfig: RemoteConfig): UpcomingEventsCallable {
    val pagedCallable by lazy { UpcomingEventsCallable(httpClient, PLAYGROUND_BASE_URL) }
    val asgCallable by lazy { UpcomingConferencesCallable(httpClient) }

    return UpcomingEventsCallable { request ->
        when {
            remoteConfig.isPagingEnabled() -> pagedCallable(request)
            else -> asgCallable(Unit).map { it.toEvent(null) }
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

private fun AsgConference.toEvent(imageUrl: String?): ApiEvent = ApiEvent(
    id = hash(), name = name, website = website, location = location, dateStart = dateStart,
    dateEnd = dateEnd, imageUrl = imageUrl, status = status, online = online,
    cfp = cfp?.let { EventCfp(start = it.start, end = it.end, site = it.site) },
)

private inline fun <reified T : Any> T.hash() = Json
    .encodeToString(this)
    .encode().md5().hex()
