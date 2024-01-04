package io.ashdavies.dominion.home

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import io.ashdavies.dominion.DominionExpansion
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.onLoading
import io.ashdavies.http.produceStateInline
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    httpClient: HttpClient = LocalHttpClient.current,
    onClick: (DominionExpansion) -> Unit = { },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val expansionService = remember(httpClient) {
        ExpansionService(httpClient)
    }

    val state by produceStateInline {
        expansionService.getExpansionList()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { CenterAlignedTopAppBar({ Text("Dominion") }) },
    ) { contentPadding ->
        state.onSuccess {
            HomeScreen(
                expansions = it.toImmutableList(),
                contentPadding = contentPadding,
                onClick = onClick,
            )
        }

        state.onLoading {
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
            )
        }

        state.onFailure {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.error)
                    .padding(contentPadding)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = it.message ?: "An unknown error occurred (${it::class.simpleName})",
                    color = MaterialTheme.colorScheme.onError,
                )
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun HomeScreen(
    expansions: ImmutableList<DominionExpansion>,
    contentPadding: PaddingValues,
    onClick: (DominionExpansion) -> Unit = { },
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
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
    value: DominionExpansion,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Box(Modifier.padding(4.dp)) {
        Image(
            painter = rememberAsyncImagePainter(value.image),
            contentDescription = value.name,
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
