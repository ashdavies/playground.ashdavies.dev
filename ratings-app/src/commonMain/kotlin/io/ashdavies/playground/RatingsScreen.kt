package io.ashdavies.playground

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun RatingsScreen(
    state: RatingsScreen.State,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()) {
        when (state) {
            is RatingsScreen.State.Loading -> for (index in 0..state.size) RatingsPlaceholder()
            is RatingsScreen.State.Idle -> for (item in state.items) RatingsItem(item)
        }
    }
}

@Composable
internal fun ColumnScope.RatingsItem(
    item: RatingsScreen.State.Item,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .weight(1f),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(item.title)
        }
    }
}

@Composable
internal fun RatingsPlaceholder(modifier: Modifier = Modifier) {
    Card(modifier.fillMaxSize()) {
        LinearProgressIndicator(modifier.fillMaxWidth())
    }
}
