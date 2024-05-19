package io.ashdavies.dominion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.slack.circuit.runtime.Navigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val DEFAULT_ASPECT_RATIO = 0.62f
private const val DEFAULT_COLUMN_COUNT = 6

private const val HORIZONTAL_CARD_SPAN = 2
private const val VERTICAL_CARD_SPAN = 3

@Composable
internal fun DetailsPresenter(
    navigator: Navigator,
    cardsStore: CardsStore,
    boxSet: BoxSet,
): DominionScreen.BoxSetDetails.State {
    var expandedCard by remember { mutableStateOf<Card?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val cards by produceState(emptyList<Card>()) {
        value = cardsStore(boxSet.title)
        isLoading = false
    }

    return DominionScreen.BoxSetDetails.State(
        boxSet = boxSet,
        cards = cards,
        expandedCard = expandedCard,
        isLoading = isLoading,
    ) { event ->
        when (event) {
            is DominionScreen.BoxSetDetails.Event.ExpandCard -> {
                expandedCard = event.card
            }

            DominionScreen.BoxSetDetails.Event.Back -> {
                navigator.pop()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun DetailsScreen(
    state: DominionScreen.BoxSetDetails.State,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = enterAlwaysScrollBehavior()
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DetailsTopBar(
                title = state.boxSet.title,
                onBack = { eventSink(DominionScreen.BoxSetDetails.Event.Back) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { contentPadding ->
        AnimatedVisibility(state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
            )
        }

        DetailsScreen(
            cards = state.cards.toImmutableList(),
            contentPadding = contentPadding,
            onClick = { eventSink(DominionScreen.BoxSetDetails.Event.ExpandCard(it)) },
        )

        AnimatedVisibility(
            visible = !state.isLoading && state.cards.isEmpty(),
            modifier = Modifier.padding(contentPadding),
        ) {
            Text(
                text = "No cards found",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error,
            )
        }
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
                    contentDescription = boxSet.name,
                    modifier = Modifier.fillMaxWidth(),
                    urlString = boxSet.image,
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
        modifier = modifier.padding(2.dp),
        contentPadding = contentPadding,
    ) {
        cards.sortedBy { it.format }.forEach {
            val currentLineSpan = columnCount / when (it.format) {
                CardFormat.HORIZONTAL -> HORIZONTAL_CARD_SPAN
                CardFormat.VERTICAL -> VERTICAL_CARD_SPAN
            }

            item(span = { GridItemSpan(currentLineSpan) }) {
                BoxSetCard(it) {
                    onClick(it)
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun BoxSetCard(
    value: Card,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Box(Modifier.padding(4.dp)) {
        Card(modifier.clickable(onClick = onClick)) {
            Image(
                painter = rememberAsyncImagePainter(value.image),
                contentDescription = value.title,
                modifier = Modifier
                    .aspectRatio(DEFAULT_ASPECT_RATIO)
                    .height(300.dp),
            )
        }
    }
}
