package io.ashdavies.dominion.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import io.ashdavies.dominion.DominionCard
import io.ashdavies.dominion.DominionRoot
import io.ashdavies.playground.RemoteImage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun CardScreen(child: DominionRoot.Child.Card) {
    Scaffold(topBar = { CardTopBar(child.card) { child.navigateToKingdom(child.card.expansion) } }) { contentPadding ->
        CardScreen(child.card, Modifier.padding(contentPadding))
    }
}

@Composable
@ExperimentalMaterial3Api
private fun CardTopBar(card: DominionCard, modifier: Modifier = Modifier, onBack: () -> Unit = { }) {
    TopAppBar(
        navigationIcon = { BackIconButton(onBack) },
        title = { Text(card.name) },
        modifier = modifier,
    )
}

@Composable
private fun BackIconButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Image(
            painter = rememberVectorPainter(Icons.Filled.ArrowBack),
            contentDescription = null,
        )
    }
}

@Composable
private fun CardScreen(card: DominionCard, modifier: Modifier = Modifier) {
    if (card.image == null) {
        Text(
            modifier = modifier.fillMaxSize(),
            text = card.name,
        )
    } else {
        RemoteImage(
            modifier = modifier.fillMaxSize(),
            urlString = card.image,
        )
    }
}
