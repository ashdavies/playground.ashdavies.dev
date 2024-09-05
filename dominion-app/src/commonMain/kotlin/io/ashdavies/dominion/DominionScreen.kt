package io.ashdavies.dominion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.slack.circuit.foundation.internal.BackHandler
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.analytics.OnClickWith
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

public fun dominionScreen(): Screen = DominionScreen.AdaptiveList

internal sealed interface DominionScreen : Parcelable, Screen {

    @Parcelize
    data object AdaptiveList : DominionScreen {
        data class State(
            val boxSetList: List<BoxSet>,
            val cardList: suspend (BoxSet) -> List<Card>,
            val isLoading: Boolean,
        ) : CircuitUiState
    }
}

private const val DEFAULT_BOX_SET_COLUMN_COUNT = 3

private const val DEFAULT_ASPECT_RATIO = 0.62f
private const val DEFAULT_DETAILS_COLUMN_COUNT = 6

private const val HORIZONTAL_CARD_SPAN = 2
private const val VERTICAL_CARD_SPAN = 3

@Composable
@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
)
internal fun DominionScreen(
    state: DominionScreen.AdaptiveList.State,
    modifier: Modifier = Modifier,
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<BoxSet>()
    val scrollBehavior = enterAlwaysScrollBehavior()

    var boxSet by remember { mutableStateOf<BoxSet?>(null) }
    var card by remember { mutableStateOf<Card?>(null) }

    BackHandler(scaffoldNavigator.canNavigateBack()) {
        scaffoldNavigator.navigateBack()
    }

    Scaffold(
        topBar = {
            DetailsTopBar(
                title = boxSet?.title ?: "Dominion",
                scrollBehavior = scrollBehavior,
                onBack = { scaffoldNavigator.navigateBack() },
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        ListDetailPaneScaffold(
            directive = scaffoldNavigator.scaffoldDirective,
            value = scaffoldNavigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    BoxSetList(
                        boxSetList = state.boxSetList.toImmutableList(),
                        contentPadding = contentPadding,
                        onClick = {
                            scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                            boxSet = it
                        },
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    scaffoldNavigator.currentDestination?.content?.let { boxSet ->
                        val cardList by produceState(emptyList<Card>()) {
                            value = state.cardList(boxSet)
                        }

                        BoxSetCardList(
                            cards = cardList.toImmutableList(),
                            contentPadding = contentPadding,
                            onClick = OnClickWith("expand_card") {
                                card = it
                            },
                        )
                    }
                }
            },
            extraPane = card?.let {
                {
                    AnimatedPane {
                        BoxSetCard(
                            value = it,
                            modifier = Modifier
                                .padding(contentPadding)
                                .fillMaxSize(),
                        ) { card = null }
                    }
                }
            },
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun DetailsTopBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: () -> Unit,
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
private fun BoxSetList(
    boxSetList: ImmutableList<BoxSet>,
    contentPadding: PaddingValues,
    columnCount: Int = DEFAULT_BOX_SET_COLUMN_COUNT,
    modifier: Modifier = Modifier,
    onClick: (BoxSet) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        modifier = modifier.padding(4.dp),
        contentPadding = contentPadding,
    ) {
        items(boxSetList) {
            BoxSet(it) { onClick(it) }
        }
    }
}

@Composable
private fun BoxSet(
    boxSet: BoxSet,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
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
            model = boxSet.image,
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
            contentDescription = boxSet.title,
            modifier = modifier
                .clickable(onClick = onClick)
                .fillMaxSize(),
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.Crop,
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
private fun BoxSetCardList(
    cards: ImmutableList<Card>,
    contentPadding: PaddingValues = PaddingValues(),
    columnCount: Int = DEFAULT_DETAILS_COLUMN_COUNT,
    onClick: (Card) -> Unit,
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
                BoxSetCard(it) { onClick(it) }
            }
        }
    }
}

@Composable
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
