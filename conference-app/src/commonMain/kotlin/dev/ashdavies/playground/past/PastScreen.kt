package dev.ashdavies.playground.past

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dev.ashdavies.parcelable.Parcelable
import dev.ashdavies.parcelable.Parcelize
import dev.ashdavies.playground.circuit.CircuitScreenKey
import dev.ashdavies.playground.events.EventsTopBar
import dev.ashdavies.playground.material.padding
import dev.ashdavies.playground.material.spacing
import dev.ashdavies.playground.material.values
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import playground.conference_app.generated.resources.Res
import playground.conference_app.generated.resources.past_events

internal object PastScreenDefaults {
    const val MIN_COLUMN_COUNT = 2
    const val MAX_COLUMN_COUNT = 5
}

@Parcelize
internal object PastScreen : Parcelable, Screen {
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
            val group: String,
            val subtitle: String,
            val attended: Boolean,
        )
    }
}

@CircuitScreenKey(PastScreen::class)
@ContributesIntoMap(AppScope::class, binding<Ui<*>>())
internal class PastUi @Inject constructor(
    private val windowSizeClass: WindowSizeClass,
) : Ui<PastScreen.State> {

    @Composable
    override fun Content(state: PastScreen.State, modifier: Modifier) {
        val columnCount = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> PastScreenDefaults.MIN_COLUMN_COUNT
            else -> PastScreenDefaults.MAX_COLUMN_COUNT
        }

        val eventSink = state.eventSink

        Scaffold(
            modifier = modifier,
            topBar = { EventsTopBar(stringResource(Res.string.past_events)) },
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(
                insets = BottomAppBarDefaults.windowInsets,
            ),
        ) { contentPadding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(columnCount),
                modifier = Modifier.padding(contentPadding),
                contentPadding = MaterialTheme.spacing.large.values,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.vertical),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.horizontal),
            ) {
                state.itemList.groupBy { it.group }.forEach { (group, items) ->
                    item(span = { GridItemSpan(columnCount) }) {
                        Text(
                            text = group,
                            modifier = Modifier.padding(MaterialTheme.spacing.medium),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }

                    items(items) { item ->
                        PastEventItem(
                            item = item,
                            modifier = Modifier
                                .clickable {
                                    eventSink(
                                        PastScreen.Event.MarkAttendance(
                                            id = item.uuid,
                                            value = !item.attended,
                                        ),
                                    )
                                }
                                .animateItem(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PastEventItem(
    item: PastScreen.State.Item,
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
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = item.title,
                modifier = Modifier.fillMaxWidth(),
                color = LocalContentColor.current,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
            )

            Text(
                text = item.subtitle,
                modifier = Modifier.fillMaxWidth(),
                color = LocalContentColor.current,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
