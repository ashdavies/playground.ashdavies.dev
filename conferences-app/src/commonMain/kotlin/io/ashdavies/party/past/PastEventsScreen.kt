package io.ashdavies.party.past

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import io.ashdavies.party.events.EventsTopBar
import io.ashdavies.party.material.LocalWindowSizeClass
import io.ashdavies.party.material.spacing
import io.ashdavies.party.material.values
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import playground.conferences_app.generated.resources.Res
import playground.conferences_app.generated.resources.past_events

internal object PastEventsDefaults {
    const val ASPECT_RATIO = 3 / 1f
}

@Parcelize
internal object PastEventsScreen : Parcelable, Screen {
    sealed interface Event {
        data class MarkAttendance(
            val id: String,
            val value: Boolean,
        ) : Event
    }

    data class State(
        val itemList: ImmutableList<Item>,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {

        data class Item(
            val uuid: String,
            val title: String,
            val subtitle: String,
            val attended: Boolean,
        )
    }
}

@Composable
internal fun PastEventsScreen(
    state: PastEventsScreen.State,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        topBar = { EventsTopBar(stringResource(Res.string.past_events)) },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(
            insets = BottomAppBarDefaults.windowInsets,
        ),
    ) { contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(
                count = when (LocalWindowSizeClass.current.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> 3
                    else -> 5
                },
            ),
            modifier = Modifier.padding(contentPadding),
            contentPadding = MaterialTheme.spacing.large.values,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.vertical),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.horizontal),
        ) {
            items(state.itemList) { item ->
                PastEventItem(
                    item = item,
                    modifier = Modifier
                        .clickable { eventSink(PastEventsScreen.Event.MarkAttendance(item.uuid, !item.attended)) }
                        .aspectRatio(PastEventsDefaults.ASPECT_RATIO)
                        .animateItem(),
                )
            }
        }
    }
}

@Composable
private fun PastEventItem(
    item: PastEventsScreen.State.Item,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = when (item.attended) {
            true -> MaterialTheme.colorScheme.surfaceContainerHighest
            false -> Color.Transparent
        },
        border = BorderStroke(
            width = 1.0.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = item.title,
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.small.horizontal)
                    .fillMaxWidth(),
                color = LocalContentColor.current,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
            )

            Text(
                text = item.subtitle,
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.small.horizontal)
                    .fillMaxWidth(),
                color = LocalContentColor.current,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
