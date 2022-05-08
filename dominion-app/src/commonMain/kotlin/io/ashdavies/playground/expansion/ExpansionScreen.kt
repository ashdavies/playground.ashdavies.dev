package io.ashdavies.playground.expansion

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ashdavies.playground.DominionExpansion
import io.ashdavies.playground.DominionRoot
import io.ashdavies.playground.DominionViewState
import io.ashdavies.playground.RemoteImage
import io.ashdavies.playground.TopAppBar
import io.ashdavies.playground.rememberInsetsPaddingValues

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ExpansionScreen(child: DominionRoot.Child.Expansion) {
    val viewModel: ExpansionViewModel = rememberExpansionViewModel()

    val _state: DominionViewState<DominionExpansion> by viewModel.state.collectAsState()
    LaunchedEffect(Unit) { viewModel.produceEvent() }

    Scaffold(topBar = { ExpansionTopAppBar() }) { contentPadding ->
        when (val state = _state) {
            is DominionViewState.Success -> ExpansionScreen(
                onClick = { child.navigateToKingdom(it) },
                contentPadding = contentPadding,
                expansions = state.value,
            )
            else -> LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun ExpansionTopAppBar() {
    TopAppBar(
        title = { Text("Dominion", color = MaterialTheme.colorScheme.onPrimary) },
        contentPadding = rememberInsetsPaddingValues(applyBottom = false)
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ExpansionScreen(
    expansions: List<DominionExpansion>,
    contentPadding: PaddingValues,
    onClick: (DominionExpansion) -> Unit = { },
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier.padding(4.dp),
        cells = GridCells.Fixed(3),
        contentPadding = contentPadding,
    ) {
        items(expansions) {
            ExpansionCard(it) { onClick(it) }
        }
    }
}

@Composable
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
