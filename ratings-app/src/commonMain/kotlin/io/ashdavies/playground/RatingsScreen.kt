package io.ashdavies.playground

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
            is RatingsScreen.State.Loading -> {
                repeat(state.size) {
                    RatingsPlaceholder()
                }
            }

            is RatingsScreen.State.Idle -> {
                for (item in state.items) RatingsItem(
                    onLongClick = { },
                    onDismiss = { },
                    onClick = { },
                    item = item
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
internal fun ColumnScope.RatingsItem(
    item: RatingsItem,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick,
            )
            .fillMaxWidth()
            .padding(4.dp)
            .weight(1f),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(item.name)
        }
    }
}

@Composable
internal fun RatingsPlaceholder(modifier: Modifier = Modifier) {
    Card(modifier.fillMaxSize()) {
        LinearProgressIndicator(modifier.fillMaxWidth())
    }
}
