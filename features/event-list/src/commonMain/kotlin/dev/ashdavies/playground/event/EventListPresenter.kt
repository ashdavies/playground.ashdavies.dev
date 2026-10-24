package dev.ashdavies.playground.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.LockClock
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
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
import dev.ashdavies.playground.ui.resources.Res
import dev.ashdavies.playground.ui.resources.cfp_closed
import dev.ashdavies.playground.ui.resources.cfp_closing
import dev.ashdavies.playground.ui.resources.cfp_open
import dev.ashdavies.playground.ui.resources.cfp_opening
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlinx.datetime.until
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.uuid.Uuid

private val EnglishMonthNameFormat = LocalDate.Format { monthName(MonthNames.ENGLISH_ABBREVIATED) }

private val YearFormat = LocalDate.Format { day() }
private val DayFormat = LocalDate.Format { day() }

private const val DEFAULT_PAGE_SIZE = 10

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
        val eventPager by produceState<Pager<Int, Event>?>(null) {
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

        var bookmarks by remember { mutableStateOf(mapOf<Uuid, Boolean>()) }
        var searchResults by remember { mutableStateOf(listOf<Event>()) }

        return EventListState.Success(
            itemList = pagingItems.itemSnapshotList
                .map { it?.toEventListStateSuccessItem(clock.todayIn(TimeZone.UTC), it.uuid in bookmarks) }
                .toPersistentList(),
            searchResults = searchResults
                .map { it.name }
                .toPersistentList(),
            isRefreshing = pagingItems.loadState.refresh is LoadState.Loading,
        ) { event ->
            when (event) {
                is EventListState.Success.Event.ToggleBookmark -> pagingItems.itemSnapshotList[event.index]?.let {
                    bookmarks += it.uuid to !(bookmarks[it.uuid] ?: false)
                }

                is EventListState.Success.Event.ItemClick -> pagingItems.itemSnapshotList[event.index]?.let {
                    remoteAnalytics.logEvent("events_click") { param("id", "${it.uuid}") }
                    navigator.goTo(EventScreen.Detail(it.uuid))
                }

                is EventListState.Success.Event.Refresh -> {
                    remoteAnalytics.logEvent("events_refresh")
                    pagingItems.refresh()
                }

                is EventListState.Success.Event.Search ->
                    searchResults = pagingItems.itemSnapshotList
                        .filterNotNull()
                        .filter { event.query in it.name }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(EventScreen.List::class, AppScope::class)
    fun interface Factory {
        fun invoke(screen: EventScreen.List, navigator: Navigator): EventListPresenter
    }
}

@Composable
public fun Event.toEventListStateSuccessItem(today: LocalDate, isBookmarked: Boolean): EventListState.Success.Item {
    val cfpStart = attrs.cfpStart?.let(LocalDate::parse)
    val cfpEnd = attrs.cfpEnd?.let(LocalDate::parse)

    return EventListState.Success.Item(
        thumbnailImageSeed = 0L,
        name = "$name '${dateStart.year % 100}",
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
        date = when {
            // Oct 31-Nov 1, 2025
            dateEnd.month > dateStart.month -> "${dateStart.format(EnglishMonthNameFormat)} " +
                    "${dateStart.format(DayFormat)}-${dateEnd.format(EnglishMonthNameFormat)} " +
                    "${dateEnd.format(DayFormat)}, ${dateEnd.format(YearFormat)}"

            // Oct 30-31, 2025
            dateEnd.day > dateStart.day -> "${dateStart.format(EnglishMonthNameFormat)} " +
                    "${dateStart.format(DayFormat)}-${dateEnd.format(DayFormat)}, " +
                    dateEnd.format(YearFormat)

            // Oct 30, 2025
            else -> "${dateStart.format(EnglishMonthNameFormat)} ${dateStart.format(DayFormat)}, " +
                    dateStart.format(YearFormat)
        },
        status = when {
            cfpStart != null -> when {
                cfpEnd != null -> when {
                    today > cfpEnd -> EventListState.Success.Item.StatusBadge(
                        text = stringResource(Res.string.cfp_closed),
                        icon = Icons.Outlined.Block,
                        containerColor = Color.Red,
                    )

                    today > cfpStart -> {
                        val hoursUntilCfpEnd = today
                            .atStartOfDayIn(TimeZone.UTC)
                            .until(
                                other = cfpEnd
                                    .plus(1, DateTimeUnit.DAY)
                                    .atStartOfDayIn(TimeZone.UTC),
                                unit = DateTimeUnit.HOUR,
                            )

                        when {
                            hoursUntilCfpEnd < 168 -> EventListState.Success.Item.StatusBadge(
                                text = stringResource(Res.string.cfp_closing, hoursUntilCfpEnd, hoursUntilCfpEnd),
                                containerColor = Color(0xFFFFA500),
                                icon = Icons.Outlined.Schedule,
                            )

                            else -> null
                        }
                    }

                    else -> null
                }

                today < cfpStart -> EventListState.Success.Item.StatusBadge(
                    text = stringResource(Res.string.cfp_open),
                    icon = Icons.Outlined.Schedule,
                    containerColor = Color.Green,
                )

                today < cfpStart -> EventListState.Success.Item.StatusBadge(
                    text = stringResource(Res.string.cfp_opening, cfpStart),
                    containerColor = Color.Unspecified,
                    icon = Icons.Outlined.LockClock,
                )

                else -> null
            }

            else -> null
        },
        isBookmarked = isBookmarked,
    )
}
