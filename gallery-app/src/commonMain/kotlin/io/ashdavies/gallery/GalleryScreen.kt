package io.ashdavies.gallery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.ashdavies.graphics.AsyncImage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun GalleryScreen(
    state: GalleryScreen.State,
    modifier: Modifier = Modifier,
) {
    val isSelecting = state is GalleryScreen.State.Success && state.itemList.any { it.selected }
    val scrollBehavior = enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { GalleryTopAppBar(scrollBehavior) },
        bottomBar = { GalleryBottomBar(state, isSelecting) },
    ) { paddingValues ->
        when (state) {
            GalleryScreen.State.Loading -> GalleryProgressIndicator()
            is GalleryScreen.State.Capture -> GalleryCapture(state.eventSink)
            is GalleryScreen.State.Empty -> GalleryEmpty()
            is GalleryScreen.State.Success -> GalleryGrid(
                itemList = state.itemList,
                isSelecting = isSelecting,
                modifier = Modifier.padding(paddingValues),
                onSelect = { state.eventSink(GalleryScreen.Event.Toggle(it)) },
            )
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
    itemList: List<GalleryScreen.State.Success.Item>,
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
            val itemPadding by animateDpAsState(
                targetValue = if (item.selected) 12.dp else 0.dp,
            )

            Box {
                val highlight = when (item.selected) {
                    false -> Modifier
                    true ->
                        Modifier
                            .padding(itemPadding)
                            .clip(RoundedCornerShape(itemPadding))
                }

                AsyncImage(
                    model = item.value.getPath(),
                    contentDescription = null,
                    modifier = highlight
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

                if (isSelecting) {
                    if (item.selected) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.padding(4.dp),
                        )
                    }

                    Canvas(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(24.dp),
                    ) {
                        drawCircle(
                            color = if (item.selected) Color.DarkGray else Color.White,
                            radius = (size.minDimension / 2) - if (item.selected) 0 else 8,
                            center = Offset(x = size.width / 2, y = size.height / 2),
                            alpha = if (item.selected) 1.0f else 0.5f,
                            style = Stroke(if (item.selected) 8f else 6f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GalleryCapture(
    eventSink: (GalleryScreen.Event) -> Unit,
    paths: FileProvider = rememberFileProvider(),
    modifier: Modifier = Modifier,
) {
    CameraOverlay(
        paths = paths,
        onCapture = { eventSink(GalleryScreen.Event.Capture) },
        modifier = modifier,
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
                IconButton(onClick = { eventSink!!(GalleryScreen.Event.Delete) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                    )
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
                FloatingActionButton(onClick = { eventSink!!(GalleryScreen.Event.Capture) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                    )
                }
            }
        },
    )
}
