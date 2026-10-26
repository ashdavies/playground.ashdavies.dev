package dev.ashdavies.playground.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.LockClock
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.MaterialTheme
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
import dev.ashdavies.playground.ui.resources.Res
import dev.ashdavies.playground.ui.resources.cfp_closed
import dev.ashdavies.playground.ui.resources.cfp_closing
import dev.ashdavies.playground.ui.resources.cfp_not_announced
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
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource
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
        thumbnailImageSeed = thumbnailImageSeed,
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
            cfpEnd != null && today > cfpEnd -> EventListState.Success.Item.StatusBadge(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                text = stringResource(Res.string.cfp_closed),
                icon = Icons.Outlined.Block,
            )

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
                    timeUntilCfpEnd != null -> EventListState.Success.Item.StatusBadge(
                        text = stringResource(
                            resource = Res.string.cfp_closing,
                            when {
                                timeUntilCfpEnd.hours < 1 -> "${timeUntilCfpEnd.minutes}m"
                                timeUntilCfpEnd.days <= 2 -> "${timeUntilCfpEnd.hours}h"
                                else -> "${timeUntilCfpEnd.days}d"
                            },
                        ),
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        icon = Icons.Outlined.Schedule,
                    )

                    else -> EventListState.Success.Item.StatusBadge(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        text = stringResource(Res.string.cfp_open),
                        icon = Icons.Outlined.Schedule,
                    )
                }
            }

            cfpStart != null && today < cfpStart -> EventListState.Success.Item.StatusBadge(
                text = stringResource(
                    resource = Res.string.cfp_opening,
                    formatDateRange(cfpStart),
                ),
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                icon = Icons.Outlined.LockClock,
            )

            else -> EventListState.Success.Item.StatusBadge(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                text = stringResource(Res.string.cfp_not_announced),
                icon = null,
            )
        },
        isBookmarked = isBookmarked,
    )
}
