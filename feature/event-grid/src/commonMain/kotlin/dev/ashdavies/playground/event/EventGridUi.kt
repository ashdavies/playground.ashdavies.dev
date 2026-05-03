package dev.ashdavies.playground.event

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import dev.ashdavies.playground.material.padding
import dev.ashdavies.playground.material.spacing
import dev.ashdavies.playground.material.values
import dev.ashdavies.playground.ui.CenterAlignedTopAppBar
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import org.jetbrains.compose.resources.stringResource
import playground.feature.event_grid.generated.resources.Res
import playground.feature.event_grid.generated.resources.past_events

internal object EventGridScreenDefaults {
    const val MIN_COLUMN_COUNT = 2
    const val MAX_COLUMN_COUNT = 5
}

@CircuitInject(EventScreen.Grid::class, AppScope::class)
internal class EventGridUi @Inject constructor() : Ui<EventGridState> {

    @Composable
    override fun Content(state: EventGridState, modifier: Modifier) {
        val isWidthAtLeastMedium = currentWindowAdaptiveInfo()
            .windowSizeClass
            .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

        val columnCount = when {
            isWidthAtLeastMedium -> EventGridScreenDefaults.MAX_COLUMN_COUNT
            else -> EventGridScreenDefaults.MIN_COLUMN_COUNT
        }

        val eventSink = state.eventSink

        Scaffold(
            modifier = modifier,
            topBar = {
                @OptIn(ExperimentalMaterial3Api::class)
                CenterAlignedTopAppBar(stringResource(Res.string.past_events))
            },
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
                        EventGridItem(
                            item = item,
                            modifier = Modifier
                                .clickable {
                                    eventSink(
                                        EventGridState.Event.MarkAttendance(
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
private fun EventGridItem(
    item: EventGridState.Item,
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
