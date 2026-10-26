package dev.ashdavies.playground.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.valentinilk.shimmer.shimmer
import dev.ashdavies.playground.material.applyIf
import dev.ashdavies.playground.material.sizing
import dev.ashdavies.playground.material.spacing
import dev.ashdavies.playground.material.uniform
import dev.ashdavies.playground.material.values
import dev.ashdavies.playground.ui.CenterAlignedTopAppBar
import dev.ashdavies.playground.ui.ErrorLayout
import dev.ashdavies.playground.ui.emptyString
import dev.ashdavies.playground.ui.gradientBrush
import dev.ashdavies.playground.ui.resources.Res
import dev.ashdavies.playground.ui.resources.upcoming_events
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
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
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium.vertical),
        ) {
            itemsIndexed(state.itemList) { index, item ->
                EventListItemContent(
                    item = item,
                    isRefreshing = state.isRefreshing,
                    onBookmarkClick = { state.eventSink(EventListState.Success.Event.ToggleBookmark(index)) },
                    onStatusClick = { state.eventSink(EventListState.Success.Event.ToggleBookmark(index)) },
                    onClick = { state.eventSink(EventListState.Success.Event.ItemClick(index)) },
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun EventListItemContent(
    item: EventListState.Success.Item?,
    isRefreshing: Boolean,
    onBookmarkClick: () -> Unit,
    onStatusClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .applyIf(isRefreshing) { it.shimmer() }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium.uniform)
                .defaultMinSize(minHeight = 80.dp)
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = MaterialTheme.spacing.medium.horizontal,
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.large)
                    .background(gradientBrush(item?.thumbnailImageSeed ?: 0L)),
            )

            Column(
                modifier = Modifier.weight(1.0f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall.vertical),
            ) {
                Text(
                    text = item?.name ?: emptyString(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = if (item != null) "${item.location} · ${item.date}" else emptyString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                item?.status?.let { status ->
                    StatusBadge(status, Modifier.padding(top = MaterialTheme.spacing.small.vertical))
                }
            }

            IconToggleButton(
                checked = item?.isBookmarked ?: false,
                onCheckedChange = { onBookmarkClick() },
                modifier = Modifier.align(Alignment.CenterVertically),
            ) {
                Icon(
                    imageVector = if (item?.isBookmarked == true) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(
    status: EventListState.Success.Item.StatusBadge,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = status.containerColor,
        contentColor = status.contentColor,
    ) {
        Row(
            modifier = Modifier.padding(
                start = if (status.icon != null) MaterialTheme.spacing.small.horizontal else MaterialTheme.spacing.medium.horizontal,
                end = MaterialTheme.spacing.medium.horizontal,
                top = MaterialTheme.spacing.small.vertical,
                bottom = MaterialTheme.spacing.small.vertical,
            ),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.horizontal),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (status.icon != null) {
                Icon(
                    imageVector = status.icon,
                    modifier = Modifier.size(MaterialTheme.sizing.icon.small),
                    contentDescription = null,
                )
            }

            Text(
                text = status.text,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
