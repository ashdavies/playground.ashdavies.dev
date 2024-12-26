package io.ashdavies.party.upcoming

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import io.ashdavies.analytics.OnClick
import io.ashdavies.party.events.Event
import io.ashdavies.party.events.EventsTopBar
import io.ashdavies.party.events.paging.errorMessage
import io.ashdavies.party.events.paging.isRefreshing
import io.ashdavies.placeholder.PlaceholderHighlight
import io.ashdavies.placeholder.fade
import io.ashdavies.placeholder.placeholder
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import playground.conferences_app.generated.resources.Res
import playground.conferences_app.generated.resources.call_for_papers
import playground.conferences_app.generated.resources.online_only
import playground.conferences_app.generated.resources.upcoming_events

private const val PLACEHOLDER_COUNT = 8
private const val EMPTY_STRING = ""

private val Today = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun UpcomingEventsPane(
    state: UpcomingEventsScreen.State,
    onClick: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRefreshing = state.pagingItems.loadState.isRefreshing

    Scaffold(
        modifier = modifier,
        topBar = {
            EventsTopBar(
                title = stringResource(Res.string.upcoming_events),
                actions = {
                    IconButton(onClick = { error("Crashlytics") }) {
                        Icon(Icons.Default.Warning, contentDescription = null)
                    }
                },
            )
        },
    ) { contentPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = OnClick("events_refresh") { state.pagingItems.refresh() },
            modifier = Modifier.padding(contentPadding),
        ) {
            if (state.pagingItems.loadState.hasError) {
                EventFailure(state.pagingItems.loadState.errorMessage ?: "Unknown Error")
            }

            LazyColumn(Modifier.fillMaxSize()) {
                val itemCount = when {
                    isRefreshing -> state.pagingItems.itemCount.coerceAtLeast(PLACEHOLDER_COUNT)
                    else -> state.pagingItems.itemCount
                }

                items(itemCount) { index ->
                    val event = state.pagingItems.rememberItemOrNull(index, isRefreshing)

                    EventItemContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp,
                            )
                            .clickable(
                                enabled = event != null,
                                onClickLabel = event?.name,
                                onClick = { onClick(event!!) },
                            )
                            .animateItem(),
                        event = event,
                    )
                }
            }
        }
    }
}

@Composable
private fun <T : Any> LazyPagingItems<T>.rememberItemOrNull(index: Int, key: Any? = null): T? {
    return remember(key, index) { if (index < itemCount) get(index) else null }
}

@Composable
private fun EventItemContent(
    event: Event?,
    modifier: Modifier = Modifier,
) {
    Card(modifier) {
        Box(Modifier.height(IntrinsicSize.Min)) {
            if (event?.imageUrl != null) {
                EventSectionBackground(
                    backgroundImageUrl = event.imageUrl,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp,
                        ),
                ) {
                    Row {
                        PlaceholderText(
                            text = event?.name,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                    PlaceholderText(
                        text = event?.location,
                        modifier = Modifier.align(Alignment.Start),
                        style = MaterialTheme.typography.titleSmall,
                    )

                    EventStatusChips(
                        cfpSite = event?.cfpSite,
                        cfpEnd = event?.cfpEnd,
                        isOnlineOnly = event?.online == true,
                    )
                }

                if (event?.dateStart != null) {
                    EventDateLabel(
                        dateStart = remember { LocalDate.parse(event.dateStart) },
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun EventDateLabel(
    dateStart: LocalDate,
    modifier: Modifier = Modifier,
) {
    Surface(modifier.clip(MaterialTheme.shapes.small)) {
        Box(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 4.dp,
            ),
        ) {
            Column {
                Text(
                    text = dateStart.format(LocalDate.Format { monthName(MonthNames.ENGLISH_ABBREVIATED) }),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.labelSmall,
                )

                Text(
                    text = dateStart.format(LocalDate.Format { dayOfMonth() }),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.labelLarge,
                )

                val currentYear = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .year

                if (dateStart.year != currentYear) {
                    Text(
                        text = dateStart.format(LocalDate.Format { year() }),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun EventStatusChips(
    cfpSite: String?,
    cfpEnd: String?,
    isOnlineOnly: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        if (cfpEnd != null) {
            val uriHandler = LocalUriHandler.current

            SuggestionChip(
                onClick = { uriHandler.openUri(requireNotNull(cfpSite)) },
                label = {
                    Text(
                        text = stringResource(Res.string.call_for_papers, cfpEnd),
                        color = LocalContentColor.current,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                enabled = cfpSite != null && LocalDate.parse(cfpEnd) > Today,
                shape = MaterialTheme.shapes.small,
            )

            HorizontalDivider(Modifier.width(8.dp))
        }

        if (isOnlineOnly) {
            SuggestionChip(
                onClick = { },
                label = {
                    Text(
                        text = stringResource(Res.string.online_only),
                        color = LocalContentColor.current,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                enabled = false,
                shape = MaterialTheme.shapes.small,
            )
        }
    }
}

@Composable
private fun EventSectionBackground(
    backgroundImageUrl: String,
    modifier: Modifier = Modifier,
    colorStopStart: Float = 0.25f,
    colorStopEnd: Float = 0.5f,
) {
    val gradientBrush = Brush.horizontalGradient(
        colorStopStart to Color.Transparent,
        colorStopEnd to Color.Black,
    )

    AsyncImage(
        model = backgroundImageUrl,
        contentDescription = null,
        modifier = modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithContent {
                drawContent()
                drawRect(gradientBrush, blendMode = BlendMode.DstIn)
            },
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun PlaceholderText(
    text: String?,
    modifier: Modifier = Modifier,
    verticalPadding: Dp = 2.dp,
    minWidth: Dp = 64.dp,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        overflow = TextOverflow.Ellipsis,
        text = text ?: EMPTY_STRING,
        style = style,
        maxLines = 1,
        modifier = modifier
            .padding(vertical = verticalPadding)
            .defaultMinSize(minWidth = minWidth)
            .placeholder(text == null, highlight = PlaceholderHighlight.fade()),
    )
}

@Composable
private fun EventFailure(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        modifier = modifier.padding(16.dp, 12.dp),
        color = MaterialTheme.colorScheme.error,
    )
}
