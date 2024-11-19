package io.ashdavies.party.events

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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.ashdavies.analytics.OnClick
import io.ashdavies.paging.LazyPagingItems
import io.ashdavies.party.material.LocalWindowSizeClass
import io.ashdavies.placeholder.PlaceholderHighlight
import io.ashdavies.placeholder.fade
import io.ashdavies.placeholder.placeholder
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import playground.conferences_app.generated.resources.Res
import playground.conferences_app.generated.resources.call_for_papers
import playground.conferences_app.generated.resources.online_only

private const val PLACEHOLDER_COUNT = 8
private const val EMPTY_STRING = ""

private val Today = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun EventsList(
    state: EventsScreen.State,
    onClick: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRefreshing = state.pagingItems.loadState.isRefreshing

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = OnClick("events_refresh") { state.pagingItems.refresh() },
        modifier = modifier,
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
                EventSection(
                    onClick = { onClick(requireNotNull(state.pagingItems[index])) },
                    modifier = Modifier.animateItem(),
                    event = state.pagingItems.rememberItemOrNull(index, isRefreshing),
                )
            }
        }
    }
}

@Composable
private fun <T : Any> LazyPagingItems<T>.rememberItemOrNull(
    index: Int,
    key: Any? = null,
): T? = remember(key, index) {
    if (index < itemCount) get(index) else null
}

@Composable
private fun EventSection(
    event: Event?,
    onClick: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            )
            .clickable(
                enabled = event != null,
                onClickLabel = event?.name,
                onClick = { onClick(event!!) },
            ),
    ) {
        EventSectionContent(
            event = event,
        )
    }
}

@Composable
private fun EventSectionContent(
    event: Event?,
    modifier: Modifier = Modifier,
    windowClassSize: WindowSizeClass = LocalWindowSizeClass.current,
) {
    Box(modifier.height(IntrinsicSize.Min)) {
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

                    if (windowClassSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
                        EventStatusChips(
                            cfpEnd = event?.cfpEnd,
                            isOnlineOnly = event?.online == true,
                            modifier = Modifier.padding(start = 12.dp),
                        )
                    }
                }

                PlaceholderText(
                    text = event?.location,
                    modifier = Modifier.align(Alignment.Start),
                    style = MaterialTheme.typography.titleSmall,
                )

                if (windowClassSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                    EventStatusChips(
                        cfpEnd = event?.cfpEnd,
                        isOnlineOnly = event?.online == true,
                    )
                }
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
                    text = dateStart.format(LocalDate.Format { monthName() }),
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
    cfpEnd: String?,
    isOnlineOnly: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        if (cfpEnd != null && LocalDate.parse(cfpEnd) > Today) {
            SuggestionChip(stringResource(Res.string.call_for_papers, cfpEnd))
            HorizontalDivider(Modifier.width(8.dp))
        }

        if (isOnlineOnly) {
            SuggestionChip(stringResource(Res.string.online_only))
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
private fun SuggestionChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    androidx.compose.material3.SuggestionChip(
        onClick = { },
        label = {
            Text(
                text = text,
                color = LocalContentColor.current,
                style = MaterialTheme.typography.labelSmall,
            )
        },
        modifier = modifier,
        enabled = false,
        shape = MaterialTheme.shapes.small,
    )
}

@Composable
internal fun PlaceholderText(
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
