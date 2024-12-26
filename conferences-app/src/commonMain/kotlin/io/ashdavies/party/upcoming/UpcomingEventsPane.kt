package io.ashdavies.party.upcoming

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
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
import io.ashdavies.party.events.Event
import io.ashdavies.party.events.EventDateLabel
import io.ashdavies.party.events.EventsTopBar
import io.ashdavies.party.events.paging.errorMessage
import io.ashdavies.party.events.paging.isRefreshing
import io.ashdavies.party.material.padding
import io.ashdavies.party.material.spacing
import io.ashdavies.party.paging.items
import io.ashdavies.placeholder.PlaceholderHighlight
import io.ashdavies.placeholder.fade
import io.ashdavies.placeholder.placeholder
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import playground.conferences_app.generated.resources.Res
import playground.conferences_app.generated.resources.call_for_papers_closed
import playground.conferences_app.generated.resources.call_for_papers_open
import playground.conferences_app.generated.resources.online_only
import playground.conferences_app.generated.resources.upcoming_events

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
    selectedItem: Event? = null,
) {
    val isRefreshing = state.pagingItems.loadState.isRefreshing

    Scaffold(
        modifier = modifier,
        topBar = { EventsTopBar(stringResource(Res.string.upcoming_events)) },
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
                items(state.pagingItems) { event ->
                    EventItemContent(
                        event = event,
                        isSelected = event == selectedItem,
                        modifier = Modifier
                            .animateItem() // TODO Slow animation on addition
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.large)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(
                                enabled = event != null,
                                onClickLabel = event?.name,
                                onClick = { onClick(event!!) },
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun EventItemContent(
    event: Event?,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.surfaceVariant
                else -> Color.Unspecified
            },
        ),
    ) {
        Box(Modifier.height(IntrinsicSize.Min)) {
            if (event?.imageUrl != null) {
                EventSectionBackground(
                    backgroundImageUrl = event.imageUrl,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.large),
            ) {
                Row {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        PlaceholderText(
                            text = event?.name,
                            style = MaterialTheme.typography.headlineSmall,
                        )

                        PlaceholderText(
                            text = event?.location,
                            modifier = Modifier.align(Alignment.Start),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }

                    if (event?.dateStart != null) {
                        Spacer(
                            modifier = Modifier.width(MaterialTheme.spacing.large.horizontal),
                        )

                        EventDateLabel(
                            dateStart = remember { LocalDate.parse(event.dateStart) },
                            dateEnd = remember { LocalDate.parse(event.dateEnd) },
                        )
                    }
                }

                EventStatusChips(
                    cfpSite = event?.cfpSite,
                    cfpEnd = event?.cfpEnd,
                    isOnlineOnly = event?.online == true,
                )
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
    Column(modifier) {
        if (cfpEnd != null && isOnlineOnly) {
            Spacer(Modifier.height(MaterialTheme.spacing.large.vertical))
        }

        Row {
            if (cfpEnd != null) {
                val daysUntilCfpEnd = Today.daysUntil(LocalDate.parse(cfpEnd))
                val uriHandler = LocalUriHandler.current

                SuggestionChip(
                    onClick = { uriHandler.openUri(requireNotNull(cfpSite)) },
                    label = {
                        Text(
                            text = when {
                                daysUntilCfpEnd > 0 -> stringResource(
                                    Res.string.call_for_papers_open,
                                    daysUntilCfpEnd,
                                )

                                else -> stringResource(Res.string.call_for_papers_closed)
                            },
                            color = LocalContentColor.current,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                    enabled = cfpSite != null && daysUntilCfpEnd > 0,
                    shape = MaterialTheme.shapes.small,
                )
            }

            if (cfpEnd != null && isOnlineOnly) {
                Spacer(Modifier.width(MaterialTheme.spacing.large.horizontal))
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
                    shape = MaterialTheme.shapes.small,
                )
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
        modifier = modifier.padding(MaterialTheme.spacing.large),
        color = MaterialTheme.colorScheme.error,
    )
}
