package io.ashdavies.tally.upcoming

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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import io.ashdavies.placeholder.PlaceholderHighlight
import io.ashdavies.placeholder.fade
import io.ashdavies.placeholder.placeholder
import io.ashdavies.tally.events.Event
import io.ashdavies.tally.events.EventDateLabel
import io.ashdavies.tally.events.EventsTopBar
import io.ashdavies.tally.events.daysUntilCfpEnd
import io.ashdavies.tally.material.padding
import io.ashdavies.tally.material.spacing
import io.ashdavies.tally.material.values
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import playground.tally_app.generated.resources.Res
import playground.tally_app.generated.resources.call_for_papers_open
import playground.tally_app.generated.resources.online_only
import playground.tally_app.generated.resources.upcoming_events

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun UpcomingEventsPane(
    state: UpcomingEventsScreen.State,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        topBar = { EventsTopBar(stringResource(Res.string.upcoming_events)) },
    ) { contentPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { eventSink(UpcomingEventsScreen.Event.Refresh) },
            modifier = Modifier.padding(contentPadding),
        ) {
            if (state.errorMessage != null) {
                EventFailure(state.errorMessage)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = MaterialTheme.spacing.large.values,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large.vertical),
            ) {
                itemsIndexed(state.itemList) { index, item ->
                    val itemModifier = Modifier
                        .animateItem() // TODO Slow animation on addition
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)

                    when (item) {
                        is Event -> EventItemContent(
                            event = item,
                            isSelected = index == state.selectedIndex,
                            modifier = itemModifier
                                .paint(rememberBackgroundPainter(item.imageUrl))
                                .clickable { onClick(index) },

                        )

                        null -> EventItemContent(
                            event = null,
                            isSelected = false,
                            modifier = itemModifier,
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
        Row(
            modifier = Modifier
                .padding(MaterialTheme.spacing.large)
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.horizontal),
        ) {
            Column(Modifier.weight(1f)) {
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

            if (event?.cfpEnd != null && daysUntilCfpEnd(LocalDate.parse(event.cfpEnd)) > 0) {
                Column {
                    EventLabel(
                        text = stringResource(Res.string.call_for_papers_open),
                        modifier = Modifier.fillMaxHeight(),
                    )
                }
            }

            if (event?.online != false) {
                Column {
                    EventLabel(
                        text = stringResource(Res.string.online_only),
                        modifier = Modifier.fillMaxHeight(),
                    )
                }
            }

            if (event?.dateStart != null) {
                Column {
                    EventDateLabel(
                        dateStart = remember { LocalDate.parse(event.dateStart) },
                        dateEnd = remember { LocalDate.parse(event.dateEnd) },
                        modifier = Modifier.fillMaxHeight(),
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
    @Suppress("UNUSED_VARIABLE")
    val brush = Brush.horizontalGradient(
        colorStopStart to Color.Transparent,
        colorStopEnd to Color.Black,
    )

    return rememberAsyncImagePainter(
        model = backgroundImageUrl,
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
        text = text ?: "",
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
