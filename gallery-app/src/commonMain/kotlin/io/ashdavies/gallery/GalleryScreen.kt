package io.ashdavies.gallery

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
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
@OptIn(ExperimentalMaterial3Api::class)
internal fun GalleryScreen(
    state: GalleryScreen.State,
    manager: StorageManager,
    modifier: Modifier = Modifier,
) {
    val isSelecting = state is GalleryScreen.State.Success && state.itemList.any { it.isSelected }
    val scrollBehavior = enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { GalleryTopAppBar(scrollBehavior) },
        bottomBar = { GalleryBottomBar(state, isSelecting) },
    ) { paddingValues ->
        when (state) {
            GalleryScreen.State.Loading -> GalleryProgressIndicator()
            is GalleryScreen.State.Empty -> GalleryEmpty()
            is GalleryScreen.State.Success -> {
                GalleryGrid(
                    itemList = state.itemList.toImmutableList(),
                    isSelecting = isSelecting,
                    modifier = Modifier.padding(paddingValues),
                    onSelect = { state.eventSink(GalleryScreen.Event.Toggle(it)) },
                )

                if (state.showCapture) {
                    GalleryCapture(
                        manager = manager,
                        eventSink = state.eventSink,
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GalleryTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String = "Gallery",
    modifier: Modifier = Modifier,
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
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun GalleryProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun GalleryEmpty(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("Empty")
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun GalleryGrid(
    itemList: ImmutableList<GalleryScreen.State.Success.Item>,
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
            val itemBorderRadius by animateDpAsState(if (item.isSelected) 12.dp else 8.dp)
            val itemPadding by animateDpAsState(if (item.isSelected) 12.dp else 0.dp)

            Box(Modifier.animateItemPlacement()) {
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(item.file),
                        contentDescription = item.name,
                        modifier = Modifier.padding(itemPadding)
                            .clip(RoundedCornerShape(itemBorderRadius))
                            .background(Color.DarkGray)
                            .combinedClickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false),
                                onLongClick = { onSelect(index) },
                                onClick = { if (isSelecting) onSelect(index) },
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
                    visible = item.isSelected,
                    modifier = Modifier.align(Alignment.TopStart),
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.padding(4.dp),
                    )

                    Canvas(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(24.dp),
                    ) {
                        drawCircle(
                            color = if (item.isSelected) Color.DarkGray else Color.White,
                            radius = (size.minDimension / 2) - if (item.isSelected) 0 else 8,
                            center = Offset(x = size.width / 2, y = size.height / 2),
                            alpha = if (item.isSelected) 1.0f else 0.5f,
                            style = Stroke(if (item.isSelected) 8f else 6f),
                        )
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
private fun GalleryCapture(
    manager: StorageManager,
    modifier: Modifier = Modifier,
    eventSink: (GalleryScreen.Event) -> Unit,
) {
    ImageCapture(
        manager = manager,
        modifier = modifier,
        onCapture = { eventSink(GalleryScreen.Event.Result(it)) },
    )
}

@Composable
private fun GalleryBottomBar(
    state: GalleryScreen.State,
    isSelecting: Boolean,
    modifier: Modifier = Modifier,
) {
    val eventSink = when (state) {
        is GalleryScreen.State.Success -> state.eventSink
        is GalleryScreen.State.Empty -> state.eventSink
        else -> null
    }

    BottomAppBar(
        actions = {
            AnimatedVisibility(
                visible = eventSink != null && isSelecting,
                enter = slideIn(initialOffset = { IntOffset(0, 200) }),
                exit = slideOut(targetOffset = { IntOffset(0, 200) }),
            ) {
                check(eventSink != null) { "Event sink cannot be null" }

                Row {
                    Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                        IconButton(
                            onClick = { eventSink(GalleryScreen.Event.Sync) },
                            enabled = state is GalleryScreen.State.Success && state.itemList.any {
                                it.state == SyncState.SYNCING
                            },
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
            AnimatedVisibility(
                visible = eventSink != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                check(eventSink != null) { "Event sink cannot be null" }

                FloatingActionButton(onClick = { eventSink(GalleryScreen.Event.Capture) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                    )
                }
            }
        },
    )
}
