package io.ashdavies.dominion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
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
import coil3.compose.rememberAsyncImagePainter
import com.slack.circuit.runtime.Navigator
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val DEFAULT_COLUMN_COUNT = 3

@Composable
internal fun ExpansionsPresenter(
    navigator: Navigator,
    httpClient: HttpClient = LocalHttpClient.current,
): DominionScreen.Expansions.State {
    var isLoading by remember { mutableStateOf(true) }

    val expansions by produceState(emptyList()) {
        value = httpClient
            .categoryMembers("Category:Sets", "page")
            .map { Expansion(it, null, null) }
            .also { isLoading = false }
    }

    return DominionScreen.Expansions.State(
        expansions = expansions,
        isLoading = false,
    ) { event ->
        when (event) {
            is DominionScreen.Expansions.Event.ShowExpansion -> {
                navigator.goTo(DominionScreen.Kingdom(event.expansion.title))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ExpansionsScreen(
    state: DominionScreen.Expansions.State,
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
            onClick = { eventSink(DominionScreen.Expansions.Event.ShowExpansion(it)) },
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
    expansions: Expansion,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Box(Modifier.padding(4.dp)) {
        if (expansions.image == null) {
            Text(expansions.title)
        }

        Image(
            painter = rememberAsyncImagePainter(expansions.image),
            contentDescription = expansions.title,
            modifier = modifier
                .fillMaxSize()
                .aspectRatio(1.0f)
                .clip(RoundedCornerShape(4.dp))
                .clickable(onClick = onClick),
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.Crop,
        )
    }
}
