package dev.ashdavies.playground.event.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.ashdavies.playground.event.EventScreen
import dev.ashdavies.playground.material.padding
import dev.ashdavies.playground.material.spacing
import dev.ashdavies.playground.material.values
import dev.ashdavies.playground.ui.CenterAlignedTopAppBar
import dev.ashdavies.playground.ui.gradientBrush
import dev.ashdavies.playground.ui.resources.Res
import dev.ashdavies.playground.ui.resources.event_history
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import org.jetbrains.compose.resources.stringResource

private const val DEFAULT_COLUMN_COUNT = 4

@Inject
@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@CircuitInject(EventScreen.History::class, AppScope::class)
public fun EventHistoryUi(state: EventHistoryState, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { CenterAlignedTopAppBar(stringResource(Res.string.event_history)) },
    ) { contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(DEFAULT_COLUMN_COUNT),
            modifier = Modifier.padding(contentPadding),
            contentPadding = MaterialTheme.spacing.large.values,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.vertical),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.horizontal),
        ) {
            itemsIndexed(state.itemList) { index, item ->
                Column {
                    Box(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.medium)
                            .clip(MaterialShapes.Cookie7Sided.toShape())
                            .background(gradientBrush(item.thumbnailImageSeed))
                            .clickable { state.eventSink(EventHistoryState.Event.ItemClick(index)) }
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "'${item.year.substring(2)}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                    Text(
                        text = item.name,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        softWrap = false,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}
