package dev.ashdavies.playground.event

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.slack.circuit.codegen.annotations.CircuitInject
import com.valentinilk.shimmer.shimmer
import dev.ashdavies.playground.material.padding
import dev.ashdavies.playground.material.spacing
import dev.ashdavies.playground.material.values
import dev.ashdavies.playground.ui.CenterAlignedTopAppBar
import dev.ashdavies.playground.ui.DateRangeBadge
import dev.ashdavies.playground.ui.DateRangeBadgeState
import dev.ashdavies.playground.ui.emptyString
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import playground.feature.event_list.generated.resources.Res
import playground.feature.event_list.generated.resources.call_for_papers_open
import playground.feature.event_list.generated.resources.online_only
import playground.feature.event_list.generated.resources.upcoming_events
import kotlin.time.Clock

@Inject
@Composable
@CircuitInject(EventScreen.List::class, AppScope::class)
public fun EventListUi(state: EventListState, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            CenterAlignedTopAppBar(stringResource(Res.string.upcoming_events))
        },
    ) { contentPadding ->
        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = { state.eventSink(EventListState.Event.Refresh) },
            modifier = Modifier.padding(contentPadding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = MaterialTheme.spacing.large.values,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large.vertical),
            ) {
                if (state.errorMessage != null) {
                    item {
                        Card(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.spacing.large),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            ),
                        ) {
                            Text(
                                text = state.errorMessage,
                                modifier = Modifier.padding(MaterialTheme.spacing.large),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }

                itemsIndexed(state.itemList) { index, item ->
                    val modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)

                    when {
                        item != null -> EventItemContent(
                            event = item,
                            isRefreshing = state.isRefreshing,
                            isSelected = index == state.selectedIndex,
                            onCfpClick = item.cfpSite?.let { { state.eventSink(EventListState.Event.ItemCfpClick(it)) } },
                            modifier = modifier
                                .clickable { state.eventSink(EventListState.Event.ItemClick(item.id)) }
                                .paint(rememberBackgroundPainter(item.imageUrl)),
                        )

                        else -> EventItemContent(
                            event = null,
                            isRefreshing = state.isRefreshing,
                            isSelected = false,
                            onCfpClick = { },
                            modifier = modifier,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventItemContent(
    event: Event?,
    isRefreshing: Boolean,
    isSelected: Boolean,
    onCfpClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = if (isRefreshing) modifier.shimmer() else modifier,
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.surfaceVariant
                else -> Color.Unspecified
            },
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(MaterialTheme.spacing.large)
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.horizontal),
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = if (event != null) {
                        val year = event?.dateStart
                            ?.let(LocalDate::parse)
                            ?.let { it.year % 100 }

                        "${event.name} '$year"
                    } else {
                        emptyString()
                    },
                    modifier = Modifier
                        .defaultMinSize(minWidth = 64.dp)
                        .padding(vertical = 2.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = event?.location ?: emptyString(),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 64.dp)
                        .padding(vertical = 2.dp),
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            event?.cfpEnd?.let { cfpEnd ->
                val today = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date

                if (today.daysUntil(LocalDate.parse(cfpEnd)) > 0) {
                    EventLabel(
                        text = stringResource(Res.string.call_for_papers_open),
                        modifier = Modifier.fillMaxHeight().then(
                            other = if (onCfpClick != null) {
                                Modifier.clickable(onClick = onCfpClick)
                            } else {
                                Modifier
                            },
                        ),
                    )
                }
            }

            if (event?.online == true) {
                Column {
                    EventLabel(
                        text = stringResource(Res.string.online_only),
                        modifier = Modifier.fillMaxHeight(),
                    )
                }
            }

            if (event?.dateStart != null) {
                Column {
                    DateRangeBadge(
                        state = remember(event.dateStart, event.dateEnd) {
                            DateRangeBadgeState(
                                dateStart = LocalDate.parse(event.dateStart),
                                dateEnd = LocalDate.parse(event.dateEnd),
                            )
                        },
                        modifier = Modifier
                            .defaultMinSize(minWidth = 64.dp)
                            .fillMaxHeight(),
                    )
                }
            }
        }
    }
}

@Composable
private fun EventLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.0.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.small)
                    .width(32.dp),
                color = LocalContentColor.current,
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Composable
private fun rememberBackgroundPainter(
    backgroundImageUrl: String?,
    colorStopStart: Float = 0.25f,
    colorStopEnd: Float = 0.5f,
): Painter {
    @Suppress("unused")
    val brush = Brush.horizontalGradient(
        colorStopStart to Color.Transparent,
        colorStopEnd to Color.Black,
    )

    return rememberAsyncImagePainter(
        model = backgroundImageUrl,
        contentScale = ContentScale.Crop,
    )
}
