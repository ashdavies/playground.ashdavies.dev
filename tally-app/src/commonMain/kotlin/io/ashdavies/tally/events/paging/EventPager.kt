package io.ashdavies.tally.events.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.ashdavies.aggregator.AsgConference
import io.ashdavies.aggregator.UpcomingConferencesCallable
import io.ashdavies.config.RemoteConfig
import io.ashdavies.config.getBoolean
import io.ashdavies.http.common.models.EventCfp
import io.ashdavies.tally.events.EventsQueries
import io.ashdavies.tally.events.callable.PagedUpcomingEventsCallable
import io.ashdavies.tally.network.todayAsString
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encode
import io.ashdavies.http.common.models.Event as ApiEvent
import io.ashdavies.tally.events.Event as DatabaseEvent

private const val PLAYGROUND_BASE_URL = "api.ashdavies.dev"
private const val DEFAULT_PAGE_SIZE = 10

private suspend fun RemoteConfig.isPagingEnabled() = getBoolean("paging_enabled")

internal typealias EventPager = Pager<String, DatabaseEvent>

@OptIn(ExperimentalPagingApi::class)
internal fun EventPager(
    eventsCallable: PagedUpcomingEventsCallable,
    eventsQueries: EventsQueries,
): EventPager {
    val pagingSourceFactory = InvalidatingPagingSourceFactory {
        EventsPagingSource(eventsQueries)
    }

    return Pager(
        config = PagingConfig(DEFAULT_PAGE_SIZE),
        initialKey = todayAsString(),
        remoteMediator = EventsRemoteMediator(
            eventsQueries = eventsQueries,
            eventsCallable = eventsCallable,
            onInvalidate = pagingSourceFactory::invalidate,
        ),
        pagingSourceFactory = pagingSourceFactory,
    )
}

internal fun PagedUpcomingEventsCallable(
    httpClient: HttpClient,
    remoteConfig: RemoteConfig,
): PagedUpcomingEventsCallable {
    val pagedCallable by lazy { PagedUpcomingEventsCallable(httpClient, PLAYGROUND_BASE_URL) }
    val asgCallable by lazy { UpcomingConferencesCallable(httpClient) }

    return PagedUpcomingEventsCallable { request ->
        when {
            remoteConfig.isPagingEnabled() -> pagedCallable(request)
            else -> asgCallable(Unit).map { it.toEvent(null) }
        }
    }
}

private fun AsgConference.toEvent(imageUrl: String?): ApiEvent = ApiEvent(
    id = hash(), name = name, website = website, location = location, dateStart = dateStart,
    dateEnd = dateEnd, imageUrl = imageUrl, status = status, online = online,
    cfp = cfp?.let { EventCfp(start = it.start, end = it.end, site = it.site) },
)

private inline fun <reified T : Any> T.hash() = Json
    .encodeToString(this)
    .encode().md5().hex()
