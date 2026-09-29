package dev.ashdavies.playground.event

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import dev.ashdavies.playground.material.applyIfNotNull
import dev.ashdavies.playground.material.padding
import dev.ashdavies.playground.material.sizing
import dev.ashdavies.playground.material.spacing
import dev.ashdavies.playground.material.values
import dev.ashdavies.playground.ui.BadgeContainer
import dev.ashdavies.playground.ui.CenterAlignedTopAppBar
import dev.ashdavies.playground.ui.DateRangeBadge
import dev.ashdavies.playground.ui.DateRangeBadgeState
import dev.ashdavies.playground.ui.ErrorLayout
import dev.ashdavies.playground.ui.Res
import dev.ashdavies.playground.ui.cfp_closed
import dev.ashdavies.playground.ui.cfp_open
import dev.ashdavies.playground.ui.emptyString
import dev.ashdavies.playground.ui.online_only
import dev.ashdavies.playground.ui.upcoming_events
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource

@Inject
@Composable
@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(EventScreen.List::class, AppScope::class)
public fun EventListUi(state: EventListState, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { CenterAlignedTopAppBar(stringResource(Res.string.upcoming_events)) },
    ) { contentPadding ->
        when (state) {
            is EventListState.Success -> EventListContent(
                state = state,
                modifier = Modifier.padding(contentPadding),
            )

            is EventListState.Failure -> ErrorLayout(
                message = state.message,
                icon = state.icon,
            )
        }
    }
}

@Composable
private fun EventListContent(
    state: EventListState.Success,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = { state.eventSink(EventListState.Success.Event.Refresh) },
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = MaterialTheme.spacing.large.values,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large.vertical),
        ) {
            itemsIndexed(state.itemList) { index, item ->
                EventListItemContent(
                    event = item,
                    isRefreshing = state.isRefreshing,
                    isSelected = item != null && index == state.selectedIndex,
                    onCfpClick = item?.cfpSite?.let {
                        { state.eventSink(EventListState.Success.Event.ItemCfpClick(it)) }
                    },
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .applyIfNotNull(item) {
                            Modifier
                                .clickable { state.eventSink(EventListState.Success.Event.ItemClick(it.id)) }
                                .paint(rememberBackgroundPainter(it.imageUrl))
                        },
                )
            }
        }
    }
}

@Composable
private fun EventListItemContent(
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
                        val year = LocalDate
                            .parse(event.dateStart)
                            .year % 100

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

            if (event?.online == true) {
                BadgeContainer(Modifier.fillMaxHeight()) {
                    Text(
                        text = stringResource(Res.string.online_only),
                        modifier = Modifier.width(MaterialTheme.sizing.icon.medium),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            event?.cfpEnd?.let { cfpEnd ->
                val daysUntilCfpEnd = daysUntilCfpEnd(LocalDate.parse(cfpEnd))

                BadgeContainer(
                    modifier = Modifier.fillMaxHeight(),
                    color = if (daysUntilCfpEnd > 0) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.surfaceDim
                    },
                ) {
                    Text(
                        text = stringResource(
                            resource = if (daysUntilCfpEnd > 0) {
                                Res.string.cfp_open
                            } else {
                                Res.string.cfp_closed
                            },
                        ),
                        modifier = Modifier.width(40.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            if (event?.dateStart != null) {
                DateRangeBadge(
                    state = remember(event.dateStart, event.dateEnd) {
                        DateRangeBadgeState(
                            dateStart = LocalDate.parse(event.dateStart),
                            dateEnd = LocalDate.parse(event.dateEnd),
                        )
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(64.dp),
                )
            }
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
