package io.ashdavies.playground

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
    val eventSink = state.eventSink

    Column(modifier.fillMaxSize()) {
        state.itemList.onEach { item ->
            when (item) {
                is RatingsScreen.State.Item.Loading -> RatingsPlaceholder()
                is RatingsScreen.State.Item.Complete -> RatingsItem(
                    item = item.value,
                    onClick = { eventSink(RatingsScreen.Event.Vote(item.value)) },
                    onLongClick = { eventSink(RatingsScreen.Event.Details(item.value)) },
                    onDismiss = { eventSink(RatingsScreen.Event.Ignore(item.value)) },
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
internal fun ColumnScope.RatingsItem(
    item: RatingsItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onDismiss: () -> Unit,
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = item.name,
            )

            Text(
                style = MaterialTheme.typography.bodySmall,
                text = item.id,
            )
        }
    }
}

@Composable
internal fun ColumnScope.RatingsPlaceholder(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .weight(1f),
    ) {
        LinearProgressIndicator(modifier.fillMaxSize())
    }
}
