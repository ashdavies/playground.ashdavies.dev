package io.ashdavies.dominion.expansion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ashdavies.http.onLoading
import io.ashdavies.http.produceStateInline
import io.ashdavies.dominion.DominionExpansion
import io.ashdavies.dominion.DominionRoot.Child.Expansion
import io.ashdavies.playground.RemoteImage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ExpansionScreen(child: Expansion, viewModel: ExpansionViewModel = rememberExpansionViewModel()) {
    val state by produceStateInline { viewModel.getViewState() }

    Scaffold(topBar = { SmallTopAppBar(title = { Text("Dominion") }) }) { contentPadding ->
        state.onSuccess {
            ExpansionScreen(
                onClick = { child.navigateToKingdom(it) },
                contentPadding = contentPadding,
                expansions = it,
            )
        }

        state.onLoading {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ExpansionScreen(
    expansions: List<DominionExpansion>,
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
    onClick: () -> Unit = { }
) {
    Box(Modifier.padding(4.dp)) {
        Card(
            content = { RemoteImage(value.image) },
            modifier = modifier
                .clickable(onClick = onClick)
                .aspectRatio(1.0f)
        )
    }
}
