package io.ashdavies.dominion.kingdom

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import io.ashdavies.dominion.DominionCard
import io.ashdavies.dominion.DominionExpansion
import io.ashdavies.dominion.layout.windowInsetsPadding
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.onLoading
import io.ashdavies.http.produceStateInline
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val DEFAULT_ASPECT_RATIO = 0.62f
private const val DEFAULT_COLUMN_COUNT = 3

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun KingdomScreen(
    expansion: DominionExpansion,
    onClick: (DominionCard) -> Unit = { },
    onBack: () -> Unit = { },
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = enterAlwaysScrollBehavior()
    val httpClient = LocalHttpClient.current
    val cardService = remember(httpClient) {
        CardService(httpClient)
    }

    val viewState by produceStateInline {
        cardService.getCardList(expansion)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { KingdomTopBar(expansion, scrollBehavior, onBack) },
    ) { contentPadding ->
        viewState.onSuccess {
            KingdomScreen(
                kingdom = it.toImmutableList(),
                contentPadding = contentPadding,
                onClick = onClick,
            )
        }

        viewState.onLoading {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun KingdomTopBar(
    expansion: DominionExpansion,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: () -> Unit = { },
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        LargeTopAppBar(
            title = { Text(expansion.name) },
            navigationIcon = {
                /*Image(
                    contentDescription = expansion.name,
                    modifier = Modifier.fillMaxWidth(),
                    urlString = expansion.image,
                )*/
                BackIconButton(onBack)
            },
            scrollBehavior = scrollBehavior,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .windowInsetsPadding(),
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
private fun KingdomScreen(
    kingdom: ImmutableList<DominionCard>,
    contentPadding: PaddingValues,
    columnCount: Int = DEFAULT_COLUMN_COUNT,
    onClick: (DominionCard) -> Unit = { },
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        modifier = modifier.padding(4.dp),
        contentPadding = contentPadding,
    ) {
        items(kingdom) {
            KingdomCard(it) { onClick(it) }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun KingdomCard(
    value: DominionCard,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Box(Modifier.padding(4.dp)) {
        Card(modifier.clickable(onClick = onClick)) {
            when (val image = value.image) {
                null -> Text(value.name, color = Color.White)
                else -> Image(
                    painter = rememberAsyncImagePainter(image),
                    contentDescription = value.name,
                    modifier = Modifier
                        .aspectRatio(DEFAULT_ASPECT_RATIO)
                        .height(300.dp),
                )
            }
        }
    }
}
