package dev.ashdavies.playground.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.paging.PagerConfig
import dev.ashdavies.paging.PagerFactory
import dev.ashdavies.playground.coroutines.retainCoroutineScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.todayIn
import kotlin.time.Clock

private val EnglishMonthNamesFormat = LocalDate.Format { monthName(MonthNames.ENGLISH_ABBREVIATED) }

private val YearFormat = LocalDate.Format { day() }
private val DayFormat = LocalDate.Format { day() }

private const val DEFAULT_PAGE_SIZE = 10

@AssistedInject
internal class EventListPresenter(
    @Assisted private val screen: EventScreen.List,
    @Assisted private val navigator: Navigator,
    private val eventPagerFactory: PagerFactory<Long, Event>,
    private val remoteAnalytics: RemoteAnalytics,
    private val clock: Clock,
) : Presenter<EventListState> {

    @Composable
    override fun present(): EventListState {
        val eventPager by produceState<Pager<Long, Event>?>(null) {
            value = eventPagerFactory.create(PagerConfig(screen.initialKey, DEFAULT_PAGE_SIZE))
        }

        val pagingItems = eventPager?.let {
            val coroutineScope = retainCoroutineScope()
            val pagingData = retain { it.flow.cachedIn(coroutineScope) }
            pagingData.collectAsLazyPagingItems()
        } ?: return EventListState.Initial

        val error = (pagingItems.loadState.refresh as? LoadState.Error)?.error
        if (error != null) {
            remoteAnalytics.recordException(error)

            return EventListState.Failure(
                icon = Icons.Outlined.CloudOff,
                message = error.message
                    ?.substringAfterLast(":")
                    ?: error.message,
            )
        }

        var sorting by remember { mutableStateOf(EventListState.Success.Sorting.DESCENDING) }
        var bookmarks by remember { mutableStateOf(mapOf<Long, Boolean>()) }
        var searchResults by remember { mutableStateOf(listOf<Event>()) }
        val todayInUtc = clock.todayIn(TimeZone.UTC)

        val itemList = when (sorting) {
            EventListState.Success.Sorting.ASCENDING -> pagingItems.itemSnapshotList.sortedBy { it?.dateStart }
            EventListState.Success.Sorting.DESCENDING -> pagingItems.itemSnapshotList.sortedByDescending { it?.dateStart }
        }

        return EventListState.Success(
            itemList = itemList
                .map { it?.toEventListStateSuccessItem(todayInUtc, it.id in bookmarks) }
                .toPersistentList(),
            searchResults = searchResults
                .map { it.name }
                .toPersistentList(),
            sorting = sorting,
            isRefreshing = pagingItems.loadState.refresh is LoadState.Loading,
        ) { event ->
            when (event) {
                is EventListState.Success.Event.ItemClick -> itemList[event.index]?.let {
                    remoteAnalytics.logEvent("events_click") { param("id", "${it.id}") }
                    navigator.goTo(EventScreen.Detail(it.id))
                }

                is EventListState.Success.Event.Refresh -> {
                    remoteAnalytics.logEvent("events_refresh")
                    pagingItems.refresh()
                }

                is EventListState.Success.Event.Search ->
                    searchResults = itemList
                        .filterNotNull()
                        .filter { event.query in it.name }

                EventListState.Success.Event.ToggleSorting -> sorting = when (sorting) {
                    EventListState.Success.Sorting.ASCENDING -> EventListState.Success.Sorting.DESCENDING
                    EventListState.Success.Sorting.DESCENDING -> EventListState.Success.Sorting.ASCENDING
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

/**
 * TODO Include mechanism for arbitrary meta data
 *
 * tracks, series, tags, attendees
 */
public fun Event.toEventListStateSuccessItem(
    today: LocalDate = Clock.System.todayIn(TimeZone.UTC),
    isBookmarked: Boolean = false,
): EventListState.Success.Item {
    val cfpStart = cfpStart?.let(LocalDate::parse)
    val cfpEnd = cfpEnd?.let(LocalDate::parse)

    val dateStart = LocalDate.parse(dateStart)
    val dateEnd = LocalDate.parse(dateEnd)

    return EventListState.Success.Item(
        status = when {
            today >= dateStart -> EventListState.Success.Item.Status.REGISTRATION_OPEN
            cfpEnd != null && today > cfpEnd -> EventListState.Success.Item.Status.CFP_CLOSED
            cfpStart != null && today >= cfpStart -> EventListState.Success.Item.Status.CFP_OPEN
            else -> null
        },
        trackCount = 3,
        series = "EMEA",
        name = "$name '${dateStart.year % 100}",
        location = location,
        dateSubtitle = when {
            today.year > dateStart.year -> "${dateStart.format(EnglishMonthNamesFormat)} ${dateStart.format(YearFormat)}"
            else -> dateStart.format(EnglishMonthNamesFormat)
        },
        dateTitle = when {
            dateEnd.day > dateStart.day -> "${dateStart.format(DayFormat)} - ${dateEnd.format(DayFormat)}"
            else -> dateStart.format(DayFormat)
        },
        tagList = persistentListOf("Compose", "KotlinMultiplatform"),
        attendeeCount = 300,
        isBookmarked = isBookmarked,
    )
}
