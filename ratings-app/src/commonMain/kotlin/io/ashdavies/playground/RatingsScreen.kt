package io.ashdavies.playground

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalAnimationApi::class)
internal fun RatingsScreen(
    state: RatingsScreen.State,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    Column(modifier.fillMaxSize()) {
        state.itemList.onEach { item ->
            /*AnimatedContent(
                modifier = Modifier.weight(1f),
                targetState = item,
            ) { item ->*/
            RatingsItem(
                item = item,
                modifier = modifier.weight(1f),
                eventSink = eventSink,
            )
            /*}*/
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
internal fun RatingsItem(
    item: RatingsScreen.State.Item,
    modifier: Modifier = Modifier,
    eventSink: (RatingsScreen.Event) -> Unit,
) {
    when (item) {
        is RatingsScreen.State.Item.Loading -> RatingsPlaceholder(modifier)
        is RatingsScreen.State.Item.Complete -> RatingsItem(
            item = item.value,
            modifier = modifier.combinedClickable(
                onLongClick = { eventSink(RatingsScreen.Event.Details(item.value)) },
                onClick = { eventSink(RatingsScreen.Event.Rate(item.value)) },
                enabled = item.rank == -1,
            ).alpha(if (item.rank == -1) 1f else 0.75f),
            onDismiss = { eventSink(RatingsScreen.Event.Ignore(item.value)) },
        )
    }
}

@Composable
internal fun RatingsItem(
    item: RatingsItem,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    Card(
        modifier = modifier.padding(4.dp),
        colors = CardDefaults.cardColors(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
        ) {
            Text(
                text = item.name,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
internal fun RatingsPlaceholder(modifier: Modifier = Modifier) {
    Card(modifier = modifier.padding(4.dp)) {
        LinearProgressIndicator(modifier.fillMaxSize())
    }
}
