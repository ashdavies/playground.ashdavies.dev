package io.ashdavies.gallery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.ashdavies.graphics.rememberAsyncImagePainter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private val Color.Companion.LightGreen: Color
    get() = Color(0xFFA5FFA5)

private val Color.Companion.Orange: Color
    get() = Color(0xFFFFA500)

@Composable
@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
internal fun GalleryScreen(
    state: GalleryScreen.State,
    manager: StorageManager,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = enterAlwaysScrollBehavior(rememberTopAppBarState())
    val isSelecting = state.itemList.any { it.isSelected }
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { GalleryTopAppBar(scrollBehavior) { } },
        bottomBar = { GalleryBottomBar(state, isSelecting) },
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
                    onSelect = { eventSink(GalleryScreen.Event.Toggle(it)) },
                )
            }
        }

        if (state.showCapture) {
            ImageCapture(manager) {
                eventSink(GalleryScreen.Event.Result(it))
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
internal fun GalleryTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String = "Gallery",
    modifier: Modifier = Modifier,
    onProfileClick: (Boolean) -> Unit,
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
        actions = { ProfileActionButton { onProfileClick(false) } },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun ProfileActionButton(
    isLoggedIn: Boolean = false,
    onClick: () -> Unit,
) {
    Crossfade(targetState = isLoggedIn) { state ->
        when (state) {
            true -> IconButton(onClick = onClick) {
                Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Logout",
                )
            }

            false -> IconButton(onClick = onClick) {
                Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Login",
                )
            }
        }
    }
}

@Composable
@ExperimentalFoundationApi
internal fun GalleryGrid(
    itemList: ImmutableList<GalleryScreen.State.Item>,
    isSelecting: Boolean = false,
    modifier: Modifier = Modifier,
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
            )
        }
    }
}

@Composable
@ExperimentalFoundationApi
internal fun GalleryItem(
    item: GalleryScreen.State.Item,
    isSelecting: Boolean = false,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
) {
    val itemBorderRadius by animateDpAsState(if (item.isSelected) 12.dp else 8.dp)
    val itemPadding by animateDpAsState(if (item.isSelected) 12.dp else 0.dp)

    Box(modifier) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(item.imageModel),
                contentDescription = item.name,
                modifier = Modifier.padding(itemPadding)
                    .clip(RoundedCornerShape(itemBorderRadius))
                    .background(Color.DarkGray)
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false),
                        onLongClick = { onSelect() },
                        onClick = { if (isSelecting) onSelect() },
                    )
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )

            Text(
                text = item.name,
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
                        modifier = Modifier.size(24.dp)
                    )

                    false -> UnselectedIndicator(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(16.dp)
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
            color = surfaceColor
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
private fun SyncIndicator(isSyncing: Boolean, modifier: Modifier = Modifier) {
    val tint by animateColorAsState(if (isSyncing) Color.Orange else Color.LightGreen)
    val scale by animateFloatAsState(if (isSyncing) 0.75f else 1f)

    var currentRotation by remember { mutableStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(isSyncing) {
        if (isSyncing) {
            rotation.animateTo(
                targetValue = currentRotation + 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 3_600,
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Restart,
                ),
                block = { currentRotation = value },
            )
        } else {
            val rotationRemaining = 180 - (currentRotation % 180)
            val targetValue = currentRotation + rotationRemaining
            val durationMillis = (rotationRemaining * 10).toInt()

            rotation.animateTo(
                targetValue = targetValue,
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = LinearOutSlowInEasing,
                ),
                block = { currentRotation = value },
            )
        }
    }

    Icon(
        imageVector = Icons.Filled.Sync,
        contentDescription = null,
        modifier = modifier
            .rotate(-rotation.value)
            .padding(4.dp)
            .scale(scale),
        tint = tint,
    )
}

@Composable
internal fun GalleryBottomBar(
    state: GalleryScreen.State,
    isSelecting: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    BottomAppBar(
        actions = {
            AnimatedVisibility(
                visible = isSelecting,
                enter = slideIn(initialOffset = { IntOffset(0, 200) }),
                exit = slideOut(targetOffset = { IntOffset(0, 200) }),
            ) {
                Row {
                    Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                        IconButton(
                            onClick = { eventSink(GalleryScreen.Event.Sync) },
                            enabled = state.itemList.none { it.state == SyncState.SYNCING },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Sync,
                                contentDescription = "Sync",
                            )
                        }
                    }

                    Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                        IconButton(onClick = { eventSink(GalleryScreen.Event.Delete) }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete",
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = { eventSink(GalleryScreen.Event.Capture) }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                )
            }
        },
    )
}
