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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import coil3.compose.AsyncImage
import io.ashdavies.analytics.OnClick
import io.ashdavies.paging.LazyPagingItems
import io.ashdavies.placeholder.PlaceholderHighlight
import io.ashdavies.placeholder.fade
import io.ashdavies.placeholder.placeholder
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
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
            itemsIndexed(
                items = state.pagingItems,
                placeholderCount = if (isRefreshing) PLACEHOLDER_COUNT else 0,
            ) { index, item ->
                EventItemContent(
                    modifier = Modifier
                        .animateItem()
                        .clickable(
                            enabled = item != null,
                            onClickLabel = item?.name,
                            onClick = { onClick(requireNotNull(state.pagingItems[index])) },
                        )
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp,
                        ),
                    event = state.pagingItems.rememberItemOrNull(index, isRefreshing),
                )
            }
        }
    }
}

private inline fun <T : Any> LazyListScope.itemsIndexed(
    items: LazyPagingItems<T>,
    placeholderCount: Int,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T?) -> Unit,
) = items(
    count = items.itemCount.coerceAtLeast(placeholderCount),
    itemContent = { itemContent(it, items[it]) },
)

@Composable
private fun <T : Any> LazyPagingItems<T>.rememberItemOrNull(
    index: Int,
    key: Any? = null,
): T? = remember(key, index) {
    if (index < itemCount) get(index) else null
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

                    if (event?.cfpEnd != null) {
                        val uriHandler = LocalUriHandler.current

                        SuggestionChip(
                            onClick = { uriHandler.openUri(requireNotNull(event.cfpSite)) },
                            label = { SmallLabelText(Res.string.call_for_papers, event.cfpEnd) },
                            enabled = event.cfpSite != null && LocalDate.parse(event.cfpEnd) > Today,
                            shape = MaterialTheme.shapes.small,
                        )
                    }

                    if (event?.online == true) {
                        SuggestionChip(
                            onClick = { },
                            label = { SmallLabelText(Res.string.online_only) },
                            enabled = false,
                            shape = MaterialTheme.shapes.small,
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
}

@Composable
private fun SmallLabelText(
    resource: StringResource,
    vararg formatArgs: Any,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(resource, formatArgs),
        modifier = modifier,
        color = LocalContentColor.current,
        style = MaterialTheme.typography.labelSmall,
    )
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
