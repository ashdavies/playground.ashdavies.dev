package io.ashdavies.dominion

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.slack.circuit.runtime.Navigator
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val DEFAULT_ASPECT_RATIO = 0.62f
private const val DEFAULT_COLUMN_COUNT = 3

@Composable
internal fun DetailsPresenter(
    navigator: Navigator,
    cardQueries: CardQueries,
    httpClient: HttpClient,
    expansion: String,
): DominionScreen.ExpansionDetails.State {
    var expandedCard by remember { mutableStateOf<Card?>(null) }

    return DominionScreen.ExpansionDetails.State(expansion, emptyList(), expandedCard) { event ->
        when (event) {
            is DominionScreen.ExpansionDetails.Event.ExpandCard -> {
                expandedCard = event.card
            }

            DominionScreen.ExpansionDetails.Event.Back -> {
                navigator.pop()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun DetailsScreen(
    state: DominionScreen.ExpansionDetails.State,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = enterAlwaysScrollBehavior()
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DetailsTopBar(
                title = state.expansion,
                onBack = { eventSink(DominionScreen.ExpansionDetails.Event.Back) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { contentPadding ->
        DetailsScreen(
            cards = state.cards.toImmutableList(),
            contentPadding = contentPadding,
            onClick = { eventSink(DominionScreen.ExpansionDetails.Event.ExpandCard(it)) },
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun DetailsTopBar(
    title: String,
    onBack: () -> Unit = { },
    scrollBehavior: TopAppBarScrollBehavior,
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        LargeTopAppBar(
            title = { Text(title) },
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .windowInsetsPadding(),
            navigationIcon = {
                /*Image(
                    contentDescription = expansion.name,
                    modifier = Modifier.fillMaxWidth(),
                    urlString = expansion.image,
                )*/
                BackIconButton(onBack)
            },
            scrollBehavior = scrollBehavior,
        )
    }
}

@Composable
private fun BackIconButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Image(
            painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
            contentDescription = null,
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun DetailsScreen(
    cards: ImmutableList<Card>,
    contentPadding: PaddingValues,
    columnCount: Int = DEFAULT_COLUMN_COUNT,
    onClick: (Card) -> Unit = { },
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        modifier = modifier.padding(4.dp),
        contentPadding = contentPadding,
    ) {
        items(cards) {
            ExpansionCard(it) { onClick(it) }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ExpansionCard(
    value: Card,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Box(Modifier.padding(4.dp)) {
        Card(modifier.clickable(onClick = onClick)) {
            when (val image = value.image) {
                null -> Text(value.title, color = Color.White)
                else -> Image(
                    painter = rememberAsyncImagePainter(image),
                    contentDescription = value.title,
                    modifier = Modifier
                        .aspectRatio(DEFAULT_ASPECT_RATIO)
                        .height(300.dp),
                )
            }
        }
    }
}
