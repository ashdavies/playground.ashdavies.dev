package io.ashdavies.party.gallery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.slack.circuit.foundation.internal.BackHandler
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.analytics.OnClick
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val DEFAULT_COLUMN_COUNT = 4

@Parcelize
internal object GalleryScreen : Parcelable, Screen {
    sealed interface Event : CircuitUiEvent {
        sealed interface Capture : Event {
            data class Result(val value: File) : Capture

            data object Cancel : Capture
            data object Request : Capture
        }

        sealed interface Selection : Event {
            data class Expand(val index: Int) : Selection
            data class Toggle(val index: Int) : Selection

            data object Collapse : Selection
            data object Delete : Selection
            data object Sync : Selection
        }
    }

    internal data class State(
        val itemList: List<StandardItem> = emptyList(),
        val expandedItem: ExpandedItem? = null,
        val showCapture: Boolean = false,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {

        data class StandardItem(
            val title: String,
            val imageModel: Any?,
            val isSelected: Boolean,
            val state: SyncState,
        )

        data class ExpandedItem(
            val contentDescription: String,
            val imageModel: Any?,
            val isExpanded: Boolean,
        )
    }
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun GalleryScreen(
    state: GalleryScreen.State,
    manager: StorageManager,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = enterAlwaysScrollBehavior(rememberTopAppBarState())
    val isSelecting = state.itemList.any { it.isSelected }
    val eventSink = state.eventSink

    Scaffold(
        floatingActionButton = {
            GalleryActionButton(
                onClick = OnClick("gallery_capture") {
                    eventSink(GalleryScreen.Event.Capture.Request)
                },
                isActive = state.showCapture,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        when {
            state.itemList.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { Text("Empty") },
                )
            }

            else -> {
                GalleryGrid(
                    itemList = state.itemList.toImmutableList(),
                    onExpand = { eventSink(GalleryScreen.Event.Selection.Expand(it)) },
                    onSelect = { eventSink(GalleryScreen.Event.Selection.Toggle(it)) },
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    isSelecting = isSelecting,
                )

                if (state.expandedItem != null) {
                    GalleryExpandedItem(state.expandedItem)

                    BackHandler(enabled = true) {
                        eventSink(GalleryScreen.Event.Selection.Collapse)
                    }
                }
            }
        }

        if (state.showCapture) {
            ImageCapture(manager) {
                when (it) {
                    is File -> eventSink(GalleryScreen.Event.Capture.Result(it))
                    null -> eventSink(GalleryScreen.Event.Capture.Cancel)
                }
            }
        }
    }
}

@Composable
private fun GalleryExpandedItem(
    expandedItem: GalleryScreen.State.ExpandedItem,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = expandedItem.isExpanded,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { },
            )
            .fillMaxSize(),
        enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
        exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center),
    ) {
        Image(
            painter = rememberAsyncImagePainter(expandedItem.imageModel),
            contentDescription = expandedItem.contentDescription,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
@ExperimentalFoundationApi
internal fun GalleryGrid(
    itemList: ImmutableList<GalleryScreen.State.StandardItem>,
    onExpand: (Int) -> Unit,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    columnCount: Int = DEFAULT_COLUMN_COUNT,
    isSelecting: Boolean = false,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        modifier = modifier,
        horizontalArrangement = spacedBy(12.dp),
        verticalArrangement = spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp),
    ) {
        itemsIndexed(itemList) { index, item ->
            GalleryItem(
                item = item,
                onSelect = { onSelect(index) },
                onExpand = { onExpand(index) },
                modifier = Modifier.animateItem(),
                isSelecting = isSelecting,
            )
        }
    }
}

@Composable
@ExperimentalFoundationApi
internal fun GalleryItem(
    item: GalleryScreen.State.StandardItem,
    onSelect: () -> Unit,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier,
    isSelecting: Boolean = false,
) {
    val itemBorderRadius by animateDpAsState(if (item.isSelected) 12.dp else 8.dp)
    val itemPadding by animateDpAsState(if (item.isSelected) 12.dp else 0.dp)

    Box(modifier) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(item.imageModel),
                contentDescription = item.title,
                modifier = Modifier.padding(itemPadding)
                    .clip(RoundedCornerShape(itemBorderRadius))
                    .background(Color.DarkGray)
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false),
                        onLongClick = { onSelect() },
                        onClick = { if (isSelecting) onSelect() else onExpand() },
                    )
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )

            Text(
                text = item.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall,
            )
        }

        AnimatedVisibility(
            visible = isSelecting,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Crossfade(item.isSelected) { state ->
                when (state) {
                    true -> SelectedIndicator(
                        modifier = Modifier.size(24.dp),
                    )

                    false -> UnselectedIndicator(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(16.dp),
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = item.state != SyncState.NOT_SYNCED,
            modifier = Modifier.align(Alignment.TopEnd),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            SyncIndicator(item.state == SyncState.SYNCING)
        }
    }
}

@Composable
private fun SelectedIndicator(
    modifier: Modifier = Modifier,
    surfaceColor: Color = MaterialTheme.colorScheme.surface,
    onPrimaryContainerColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    iconPainter: Painter = rememberVectorPainter(Icons.Filled.CheckCircle),
) {
    Canvas(modifier) {
        drawCircle(
            color = surfaceColor,
        )

        with(iconPainter) {
            draw(
                size = iconPainter.intrinsicSize,
                colorFilter = ColorFilter.tint(onPrimaryContainerColor),
            )
        }
    }
}

@Composable
private fun UnselectedIndicator(
    modifier: Modifier = Modifier,
    highlightColor: Color = Color.White.copy(alpha = 0.5F),
    strokeWidth: Dp = 2.dp,
) {
    Canvas(modifier) {
        drawCircle(
            color = highlightColor,
            radius = (size.minDimension / 2.0f),
            style = Stroke(strokeWidth.toPx()),
        )
    }
}

@Composable
private fun GalleryActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    imageVector: ImageVector = Icons.Filled.Add,
    contentDescription: String? = "Add",
) {
    FloatingActionButton(
        onClick = { if (!isActive) onClick() },
        modifier = modifier,
    ) {
        Crossfade(targetState = isActive) { state ->
            when (state) {
                true -> CircularProgressIndicator(
                    modifier = Modifier.size(imageVector.defaultWidth),
                )

                false -> Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                )
            }
        }
    }
}
