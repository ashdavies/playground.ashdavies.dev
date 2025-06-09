package io.ashdavies.tally.events.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.ashdavies.asg.AsgConference
import io.ashdavies.asg.UpcomingConferencesCallable
import io.ashdavies.config.RemoteConfig
import io.ashdavies.config.getBoolean
import io.ashdavies.http.common.models.EventCfp
import io.ashdavies.tally.events.Conference
import io.ashdavies.tally.events.ConferenceQueries
import io.ashdavies.tally.events.callable.PagedUpcomingConferencesCallable
import io.ashdavies.tally.events.callable.PagedUpcomingEventsCallable
import io.ashdavies.tally.network.todayAsString
import io.ktor.client.*
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encode
import io.ashdavies.http.common.models.Event as ApiEvent

private const val PLAYGROUND_BASE_URL = "api.ashdavies.dev"
private const val DEFAULT_PAGE_SIZE = 10

private suspend fun RemoteConfig.isPagingEnabled() = getBoolean("paging_enabled")

internal typealias EventPager = Pager<Long, Conference>

@OptIn(ExperimentalPagingApi::class)
internal fun ConferencePager(
    conferencesCallable: PagedUpcomingConferencesCallable,
    conferenceQueries: ConferenceQueries,
): EventPager {
    val pagingSourceFactory = InvalidatingPagingSourceFactory {
        ConferencePagingSource(conferenceQueries)
    }

    return Pager(
        config = PagingConfig(DEFAULT_PAGE_SIZE),
        initialKey = 0L,
        remoteMediator = ConferencesRemoteMediator(
            conferenceQueries = conferenceQueries,
            eventsCallable = conferencesCallable,
            onInvalidate = pagingSourceFactory::invalidate,
        ),
        pagingSourceFactory = pagingSourceFactory,
    )
}

internal fun PagedUpcomingEventsCallable(
    httpClient: HttpClient,
    remoteConfig: RemoteConfig,
): PagedUpcomingConferencesCallable {
    val pagedCallable by lazy { PagedUpcomingEventsCallable(httpClient, PLAYGROUND_BASE_URL) }
    val asgCallable by lazy { UpcomingConferencesCallable(httpClient) }

    return PagedUpcomingConferencesCallable { request ->
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
