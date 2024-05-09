package io.ashdavies.dominion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.slack.circuit.runtime.Navigator
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val DEFAULT_COLUMN_COUNT = 3

@Composable
internal fun ExpansionsPresenter(
    navigator: Navigator,
    expansionsQueries: ExpansionQueries,
    httpClient: HttpClient,
): DominionScreen.ExpansionsList.State {
    var isLoading by remember { mutableStateOf(true) }
    val expansionsStore =
        remember { ExpansionsStore(expansionsQueries, httpClient, refresh = true) }
    val expansions by produceState(emptyList<Expansion>()) {
        value = expansionsStore()
        isLoading = false
    }

    return DominionScreen.ExpansionsList.State(
        expansions = expansions,
        isLoading = isLoading,
    ) { event ->
        when (event) {
            is DominionScreen.ExpansionsList.Event.ShowExpansion -> {
                navigator.goTo(DominionScreen.ExpansionDetails(event.expansion.title))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ExpansionsScreen(
    state: DominionScreen.ExpansionsList.State,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { CenterAlignedTopAppBar({ Text("Dominion") }) },
    ) { contentPadding ->
        AnimatedVisibility(state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
            )
        }

        ExpansionsScreen(
            expansions = state.expansions.toImmutableList(),
            contentPadding = contentPadding,
            onClick = { eventSink(DominionScreen.ExpansionsList.Event.ShowExpansion(it)) },
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ExpansionsScreen(
    expansions: ImmutableList<Expansion>,
    contentPadding: PaddingValues,
    columnCount: Int = DEFAULT_COLUMN_COUNT,
    onClick: (Expansion) -> Unit = { },
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        modifier = modifier.padding(4.dp),
        contentPadding = contentPadding,
    ) {
        items(expansions) {
            ExpansionCard(it) { onClick(it) }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ExpansionCard(
    expansion: Expansion,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Box(
        modifier = modifier
            .aspectRatio(1.0f)
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        var isLoading by remember { mutableStateOf(true) }
        val painter = rememberAsyncImagePainter(
            model = expansion.image,
            onState = { isLoading = it is AsyncImagePainter.State.Loading },
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = modifier
                    .fillMaxSize()
                    .padding(48.dp),
            )
        }

        Image(
            painter = painter,
            contentDescription = expansion.title,
            modifier = modifier
                .clickable(onClick = onClick)
                .fillMaxSize(),
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.Crop,
        )
    }
}
