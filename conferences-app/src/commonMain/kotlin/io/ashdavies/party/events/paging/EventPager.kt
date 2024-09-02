package io.ashdavies.party.events.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.ashdavies.aggregator.AsgConference
import io.ashdavies.aggregator.UpcomingConferencesCallable
import io.ashdavies.config.RemoteConfig
import io.ashdavies.config.getBoolean
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.common.models.EventCfp
import io.ashdavies.party.events.EventsQueries
import io.ashdavies.party.events.callable.PagedUpcomingEventsCallable
import io.ashdavies.party.network.todayAsString
import io.ashdavies.party.sql.rememberLocalQueries
import io.ktor.client.HttpClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encode
import io.ashdavies.http.common.models.Event as ApiEvent
import io.ashdavies.party.events.Event as DatabaseEvent

private const val PLAYGROUND_BASE_URL = "playground.ashdavies.dev"
private const val DEFAULT_PAGE_SIZE = 10

private suspend fun RemoteConfig.isPagingEnabled() = getBoolean("paging_enabled")

@Composable
@ExperimentalPagingApi
internal fun rememberEventPager(
    eventsQueries: EventsQueries = rememberLocalQueries { it.eventsQueries },
    eventsCallable: PagedUpcomingEventsCallable = rememberUpcomingEventsCallable(),
    initialKey: String = todayAsString(),
    pageSize: Int = DEFAULT_PAGE_SIZE,
): Pager<String, DatabaseEvent> = remember(eventsQueries, eventsCallable) {
    val pagingSourceFactory = InvalidatingPagingSourceFactory {
        EventsPagingSource(eventsQueries)
    }

    val remoteMediator = EventsRemoteMediator(
        eventsQueries = eventsQueries,
        eventsCallable = eventsCallable,
        onInvalidate = pagingSourceFactory::invalidate,
    )

    Pager(
        config = PagingConfig(pageSize),
        initialKey = initialKey,
        remoteMediator = remoteMediator,
        pagingSourceFactory = pagingSourceFactory,
    )
}

@Composable
private fun rememberUpcomingEventsCallable(
    httpClient: HttpClient = LocalHttpClient.current,
    remoteConfig: RemoteConfig = RemoteConfig,
): PagedUpcomingEventsCallable {
    val pagedCallable by lazy { PagedUpcomingEventsCallable(httpClient, PLAYGROUND_BASE_URL) }
    val asgCallable by lazy { UpcomingConferencesCallable(httpClient) }

    return PagedUpcomingEventsCallable { request ->
        if (remoteConfig.isPagingEnabled()) pagedCallable(request)
        else asgCallable(Unit).map(AsgConference::toEvent)
    }
}

private fun AsgConference.toEvent(): ApiEvent = ApiEvent(
    id = hash(), name = name, website = website, location = location, dateStart = dateStart,
    dateEnd = dateEnd, imageUrl = imageUrl, status = status, online = online,
    cfp = cfp?.let { EventCfp(start = it.start, end = it.end, site = it.site) },
)

private inline fun <reified T : Any> T.hash() = Json
    .encodeToString(this)
    .encode().md5().hex()
