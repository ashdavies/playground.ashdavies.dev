package io.ashdavies.gallery

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
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
import com.slack.circuit.foundation.internal.BackHandler
import io.ashdavies.graphics.rememberAsyncImagePainter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

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

    BottomSheetScaffold(
        sheetContent = {
            GallerySheetContent(
                eventSink = eventSink,
                modifier = Modifier.padding(16.dp),
            )
        },
        showDragHandle = false,
        topBar = {
            GalleryTopAppBar(state.authState, scrollBehavior) {
                eventSink(GalleryScreen.Event.Identity.SignIn)
            }
        },
        floatingActionButton = {
            GalleryActionButton(
                onClick = { eventSink(GalleryScreen.Event.Capture.Request) },
                isActive = state.showCapture,
            )
        },
        isExpanded = isSelecting,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        when {
            state.itemList.isEmpty() -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { Text("Empty") },
                )
            }

            else -> {
                GalleryGrid(
                    itemList = state.itemList.toImmutableList(),
                    isSelecting = isSelecting,
                    modifier = Modifier.padding(paddingValues),
                    onExpand = { eventSink(GalleryScreen.Event.Selection.Expand(it)) },
                    onSelect = { eventSink(GalleryScreen.Event.Selection.Toggle(it)) },
                )

                if (state.expandedItem != null) {
                    AnimatedVisibility(
                        visible = state.expandedItem.isExpanded,
                        modifier = Modifier
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
                            painter = rememberAsyncImagePainter(state.expandedItem.imageModel),
                            contentDescription = state.expandedItem.contentDescription,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .fillMaxSize(),
                            contentScale = ContentScale.Fit,
                        )
                    }
                }

                BackHandler(state.expandedItem != null) {
                    eventSink(GalleryScreen.Event.Selection.Collapse)
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
@ExperimentalMaterial3Api
internal fun GalleryTopAppBar(
    authState: AuthState,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String = "Gallery",
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        modifier = modifier,
        actions = { ProfileActionButton(authState) { onProfileClick() } },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun ProfileActionButton(
    authState: AuthState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Crossfade(authState, modifier) { state ->
        when (state) {
            is AuthState.Authenticated -> IconButton(onClick = onClick) {
                Image(
                    painter = rememberAsyncImagePainter(state.profilePictureUrl),
                    contentDescription = null,
                    modifier = Modifier.clip(CircleShape),
                )
            }

            AuthState.Unauthenticated -> IconButton(onClick = onClick) {
                Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "SignIn",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                )
            }

            AuthState.Unsupported -> Unit
        }
    }
}

@Composable
@ExperimentalFoundationApi
internal fun GalleryGrid(
    itemList: ImmutableList<GalleryScreen.State.StandardItem>,
    isSelecting: Boolean = false,
    modifier: Modifier = Modifier,
    onExpand: (Int) -> Unit,
    onSelect: (Int) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = spacedBy(12.dp),
        verticalArrangement = spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp),
    ) {
        itemsIndexed(itemList) { index, item ->
            GalleryItem(
                item = item,
                isSelecting = isSelecting,
                modifier = Modifier.animateItemPlacement(),
                onSelect = { onSelect(index) },
                onExpand = { onExpand(index) },
            )
        }
    }
}

@Composable
@ExperimentalFoundationApi
internal fun GalleryItem(
    item: GalleryScreen.State.StandardItem,
    isSelecting: Boolean = false,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
    onExpand: () -> Unit,
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
                        indication = rememberRipple(bounded = false),
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
    surfaceColor: Color = MaterialTheme.colorScheme.surface,
    onPrimaryContainerColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    iconPainter: Painter = rememberVectorPainter(Icons.Filled.CheckCircle),
    modifier: Modifier = Modifier,
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
    highlightColor: Color = Color.White.copy(alpha = 0.5F),
    strokeWidth: Dp = 2.dp,
    modifier: Modifier = Modifier,
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

@Composable
internal fun GallerySheetContent(
    eventSink: (GalleryScreen.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        BottomSheetItem(
            imageVector = Icons.Filled.Sync,
            onClick = { eventSink(GalleryScreen.Event.Selection.Sync) },
            contentDescription = "Sync",
            modifier = Modifier.padding(8.dp),
        )

        BottomSheetItem(
            imageVector = Icons.Filled.Delete,
            onClick = { eventSink(GalleryScreen.Event.Selection.Delete) },
            contentDescription = "Delete",
            modifier = Modifier.padding(8.dp),
        )
    }
}
