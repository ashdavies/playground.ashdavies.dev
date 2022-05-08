package io.ashdavies.playground.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import io.ashdavies.playground.DominionCard
import io.ashdavies.playground.DominionRoot
import io.ashdavies.playground.RemoteImage
import io.ashdavies.playground.TopAppBar
import io.ashdavies.playground.rememberInsetsPaddingValues

@Composable
internal fun CardScreen(child: DominionRoot.Child.Card) {
    Scaffold(topBar = { CardTopBar(child.card) { child.navigateToKingdom(child.card.expansion) } }) { contentPadding ->
        CardScreen(child.card, Modifier.padding(contentPadding))
    }
}

@Composable
private fun CardTopBar(
    card: DominionCard,
    contentPadding: PaddingValues = rememberInsetsPaddingValues(applyBottom = false),
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { }
) {
    Surface(
        tonalElevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier,
    ) {
        TopAppBar(
            navigationIcon = { BackIconButton(onBack) },
            modifier = modifier.padding(contentPadding),
            title = { Text(card.name) },
        )
    }
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
            urlString = card.image
        )
    }
}
