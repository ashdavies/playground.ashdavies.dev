package io.ashdavies.events

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.analytics.OnClick
import io.ashdavies.paging.LazyPagingItems
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import io.ashdavies.placeholder.PlaceholderHighlight
import io.ashdavies.placeholder.fade
import io.ashdavies.placeholder.placeholder
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private const val EMPTY_STRING = ""

private val Today = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date

private enum class EventEmphasis {
    High,
    Mid,
    Standard,
}

@Parcelize
internal object EventsScreen : Parcelable, Screen {
    data class State(val pagingItems: LazyPagingItems<Event>) : CircuitUiState
}

@Composable
@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class,
)
internal fun EventsScreen(
    state: EventsScreen.State,
    modifier: Modifier = Modifier,
    showPlaceholders: Int = 8,
) {
    val isRefreshing = state.pagingItems.loadState.isRefreshing
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = OnClick("events_refresh") {
            state.pagingItems.refresh()
        },
    )

    Box(modifier.pullRefresh(pullRefreshState)) {
        if (state.pagingItems.loadState.hasError) {
            EventFailure(state.pagingItems.loadState.errorMessage ?: "Unknown Error")
        }

        LazyColumn(Modifier.fillMaxSize()) {
            val itemCount = when (isRefreshing) {
                true -> state.pagingItems.itemCount.coerceAtLeast(showPlaceholders)
                false -> state.pagingItems.itemCount
            }

            items(itemCount) { index ->
                EventSection(
                    event = state.pagingItems.getOrNull(index),
                    modifier = Modifier.animateItemPlacement(),
                    emphasis = when (index) {
                        0 -> EventEmphasis.High
                        1 -> EventEmphasis.Mid
                        else -> EventEmphasis.Standard
                    },
                )
            }
        }

        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = pullRefreshState,
        )
    }
}

private fun <T : Any> LazyPagingItems<T>.getOrNull(index: Int): T? {
    return if (index < itemCount) get(index) else null
}

@Composable
@ExperimentalMaterialApi
private fun EventSection(
    event: Event?,
    modifier: Modifier = Modifier,
    emphasis: EventEmphasis,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
    ) {
        Box(Modifier.height(IntrinsicSize.Min)) {
            if (event?.imageUrl != null) AsyncImage(
                model = event.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.horizontalGradient(
                                0.25f to Color.Transparent,
                                0.5f to Color.Black,
                            ),
                            blendMode = BlendMode.DstIn,
                        )
                    },
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
            ) {
                PlaceholderText(
                    text = event?.name,
                    modifier = Modifier.align(Alignment.Start),
                    style = when (emphasis) {
                        EventEmphasis.High -> MaterialTheme.typography.headlineLarge
                        EventEmphasis.Mid -> MaterialTheme.typography.headlineMedium
                        EventEmphasis.Standard -> MaterialTheme.typography.headlineSmall
                    },
                )

                PlaceholderText(
                    text = event?.location,
                    modifier = Modifier.align(Alignment.Start),
                    style = when (emphasis) {
                        EventEmphasis.High -> MaterialTheme.typography.titleLarge
                        EventEmphasis.Mid -> MaterialTheme.typography.titleMedium
                        EventEmphasis.Standard -> MaterialTheme.typography.titleSmall
                    },
                )

                PlaceholderText(
                    text = event?.dateStart,
                    modifier = Modifier.align(Alignment.Start),
                    style = when (emphasis) {
                        EventEmphasis.High -> MaterialTheme.typography.labelLarge
                        EventEmphasis.Mid -> MaterialTheme.typography.labelMedium
                        EventEmphasis.Standard -> MaterialTheme.typography.labelSmall
                    },
                )

                Row {
                    if (event?.cfpEnd != null && LocalDate.parse(event.cfpEnd) > Today) {
                        TextChip(
                            text = "Call for Papers (Until ${event.cfpEnd})",
                            modifier = Modifier.padding(end = 8.dp),
                            onClick = OnClick("event_cfp") { },
                        )
                    }

                    if (event?.online == true) {
                        TextChip(
                            text = "Online Only",
                            enabled = false,
                            onClick = { },
                        )
                    }
                }

            }
        }
    }
}

@Composable
@ExperimentalMaterialApi
private fun TextChip(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit,
) {
    Chip(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
    ) {
        Text(
            text = text,
            color = LocalContentColor.current,
        )
    }
}

@Composable
internal fun PlaceholderText(
    text: String?,
    modifier: Modifier = Modifier,
    verticalPadding: Dp = 2.dp,
    characters: Int = 12,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        overflow = TextOverflow.Ellipsis,
        text = text ?: EMPTY_STRING,
        style = style,
        maxLines = 1,
        modifier = modifier
            .padding(vertical = verticalPadding)
            .defaultMinSize(minWidth = Dp(style.fontSize.value * characters))
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
