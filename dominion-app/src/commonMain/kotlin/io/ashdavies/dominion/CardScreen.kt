package io.ashdavies.dominion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import coil3.compose.rememberAsyncImagePainter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun CardScreen(
    card: Card,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { CardTopBar(card, onBack = onBack) },
    ) { contentPadding ->
        CardScreen(card, Modifier.padding(contentPadding))
    }
}

@Composable
@ExperimentalMaterial3Api
private fun CardTopBar(
    card: Card,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { },
) {
    TopAppBar(
        navigationIcon = { BackIconButton(onBack) },
        title = { Text(card.title) },
        modifier = modifier,
    )
}

@Composable
private fun BackIconButton(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Image(
            painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
            contentDescription = null,
        )
    }
}

@Composable
private fun CardScreen(card: Card, modifier: Modifier = Modifier) {
    if (card.image == null) {
        Text(
            modifier = modifier.fillMaxSize(),
            text = card.title,
        )
    } else {
        Image(
            painter = rememberAsyncImagePainter(card.image),
            contentDescription = card.title,
            modifier = modifier.fillMaxSize(),
        )
    }
}
