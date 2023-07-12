package io.ashdavies.gallery

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import io.ashdavies.graphics.AsyncImage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun GalleryScreen(
    state: GalleryScreen.State,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { GalleryTopAppBar(scrollBehavior) },
        floatingActionButton = { GalleryActionButton(state) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        when (state) {
            GalleryScreen.State.Loading -> GalleryProgressIndicator()
            is GalleryScreen.State.Empty -> GalleryEmpty()
            is GalleryScreen.State.Success -> GalleryGrid(
                itemList = state.itemList,
                eventSink = state.eventSink,
                modifier = Modifier.padding(paddingValues)
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
            containerColor = MaterialTheme.colorScheme.background
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
private fun GalleryGrid(
    itemList: List<Uri>,
    eventSink: (GalleryScreen.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(itemList) {
            AsyncImage(
                model = it,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun GalleryActionButton(state: GalleryScreen.State) {
    val eventSink = when (state) {
        is GalleryScreen.State.Success -> state.eventSink
        is GalleryScreen.State.Empty -> state.eventSink
        else -> null
    }

    if (eventSink != null) {
        FloatingActionButton(onClick = { eventSink(GalleryScreen.Event.Capture) }) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}
