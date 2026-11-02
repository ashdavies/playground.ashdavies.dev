package dev.ashdavies.playground.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.paging.PagerConfig
import dev.ashdavies.paging.PagerFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.uuid.Uuid

private const val DEFAULT_PAGE_SIZE = 10

private val Event.thumbnailImageSeed: Long
    get() = "$name$location"
        .hashCode()
        .toLong()

@AssistedInject
internal class EventListPresenter(
    @Assisted private val screen: EventScreen.List,
    @Assisted private val navigator: Navigator,
    private val eventPagerFactory: PagerFactory<Int, Event>,
    private val remoteAnalytics: RemoteAnalytics,
    private val clock: Clock,
) : Presenter<EventListState> {

    @Composable
    override fun present(): EventListState {
        val pagingData by produceRetainedState<Flow<PagingData<Event>>?>(null) {
            value = eventPagerFactory
                .create(PagerConfig(screen.initialKey, DEFAULT_PAGE_SIZE))
                .flow
        }

        var bookmarks by remember { mutableStateOf(mapOf<Uuid, Boolean>()) }
        val today = clock.todayIn(TimeZone.UTC)

        val pagingItems = (pagingData ?: return EventListState.Failure(Icons.Outlined.CloudDownload, null))
            .map { data -> data.map { it.toEventListStateSuccessItem(today, bookmarks[it.uuid] ?: false) } }
            .collectAsLazyPagingItems()

        val error = pagingItems.loadState.refresh
            .let { it as? LoadState.Error }
            ?.error

        if (error != null) {
            remoteAnalytics.recordException(error)

            return EventListState.Failure(
                icon = Icons.Outlined.CloudOff,
                message = error.message
                    ?.substringAfterLast(":")
                    ?: error.message,
            )
        }

        return EventListState.Success(
            itemList = pagingItems,
            isRefreshing = pagingItems.loadState.refresh is LoadState.Loading,
        ) { event ->
            when (event) {
                is EventListState.Success.Event.ToggleBookmark -> pagingItems[event.index]?.let {
                    bookmarks += it.uuid to !(bookmarks[it.uuid] ?: false)
                }

                is EventListState.Success.Event.ItemClick -> pagingItems[event.index]?.let {
                    remoteAnalytics.logEvent("events_click") { param("id", "${it.uuid}") }
                    navigator.goTo(EventScreen.Detail(it.uuid))
                }

                is EventListState.Success.Event.Refresh -> {
                    remoteAnalytics.logEvent("events_refresh")
                    pagingItems.refresh()
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(EventScreen.List::class, AppScope::class)
    fun interface Factory {
        fun invoke(screen: EventScreen.List, navigator: Navigator): EventListPresenter
    }
}

public fun Event.toEventListStateSuccessItem(today: LocalDate, isBookmarked: Boolean): EventListState.Success.Item {
    val cfpStart = attrs.cfpStart?.let(LocalDate::parse)
    val cfpEnd = attrs.cfpEnd?.let(LocalDate::parse)

    return EventListState.Success.Item(
        thumbnailImageSeed = thumbnailImageSeed,
        uuid = uuid,
        name = "$name ${dateStart.year}",
        location = location.split(", ").let { (city, country) ->
            when (country) {
                "Czech Republic" -> "🇨🇿 $city"
                "Germany" -> "🇩🇪 $city"
                "Kenya" -> "🇰🇪 $city"
                "India" -> "🇮🇳 $city"
                "Netherlands" -> "🇳🇱 $city"
                "Portugal" -> "🇵🇹 $city"
                "Uganda" -> "🇺🇬 $city"
                "UK" -> "🇬🇧 $city"
                else -> "📍 $city"
            }
        },
        date = formatDateRange(dateStart, dateEnd),
        status = when {
            cfpEnd != null && today > cfpEnd -> EventListState.Success.Item.Status.Closed

            cfpStart != null && today > cfpStart -> {
                val timeUntilCfpEnd = cfpEnd?.let { cfpEnd ->
                    today
                        .atStartOfDayIn(TimeZone.UTC)
                        .periodUntil(
                            other = cfpEnd
                                .plus(1, DateTimeUnit.DAY)
                                .atStartOfDayIn(TimeZone.UTC),
                            timeZone = TimeZone.UTC,
                        )
                }

                when {
                    timeUntilCfpEnd != null -> EventListState.Success.Item.Status.Closing(
                        remaining = when {
                            timeUntilCfpEnd.hours < 1 -> "${timeUntilCfpEnd.minutes}m"
                            timeUntilCfpEnd.days <= 2 -> "${timeUntilCfpEnd.hours}h"
                            else -> "${timeUntilCfpEnd.days}d"
                        },
                    )

                    else -> EventListState.Success.Item.Status.Open
                }
            }

            cfpStart != null && today < cfpStart -> EventListState.Success.Item.Status.Opening(formatDateRange(cfpStart))

            else -> EventListState.Success.Item.Status.NotAnnounced
        },
        isBookmarked = isBookmarked,
    )
}
