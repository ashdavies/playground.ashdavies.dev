package io.ashdavies.playground.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dropbox.android.external.store4.Store
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.ashdavies.playground.common.viewModel
import io.ashdavies.playground.compose.fade
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.emptyString
import io.ashdavies.playground.events.EventsViewState.Failure
import io.ashdavies.playground.events.EventsViewState.Loading
import io.ashdavies.playground.events.EventsViewState.Section
import io.ashdavies.playground.events.EventsViewState.Success
import io.ashdavies.playground.events.EventsViewState.Uninitialised
import io.ashdavies.playground.network.EventsService
import io.ashdavies.playground.network.LocalHttpClient
import kotlinx.coroutines.FlowPreview

@Preview
@Composable
@OptIn(FlowPreview::class)
internal fun EventsScreen() {
    val eventsStore: Store<Unit, List<Event>> = LocalEventsStore.current
    val viewModel: EventsViewModel = viewModel { EventsViewModel(eventsStore) }
    val viewState: EventsViewState by viewModel
        .viewState
        .collectAsState(Uninitialised)

    SwipeRefresh(
        state = rememberSwipeRefreshState(viewState is Loading),
        modifier = Modifier.fillMaxSize(),
        onRefresh = viewModel::onRefresh,
    ) {
        when (val it: EventsViewState = viewState) {
            is Loading -> EventsList(List(10) { null }) {}
            is Failure -> EventFailure(it.message)
            is Success -> EventsList(it.data) {
                println("Clicked $it")
            }
            else -> Unit
        }
    }
}

@Composable
internal fun EventsList(data: List<Section?>, modifier: Modifier = Modifier, onClick: () -> Unit) {
    LazyColumn(
        contentPadding = LocalScaffoldPadding.current,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {
        items(data) { EventSection(it, onClick) }
    }
}

@Composable
internal fun EventSection(section: Section?, onClick: () -> Unit) {
    Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp)
                ) {
                    PlaceholderText(section?.name, style = MaterialTheme.typography.h6)
                    PlaceholderText(section?.location, style = MaterialTheme.typography.body1)
                    PlaceholderText(section?.date, style = MaterialTheme.typography.body2)
                }

            }
        }
    }
}

@Composable
private fun ColumnScope.PlaceholderText(
    text: String?,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        overflow = TextOverflow.Ellipsis,
        text = text ?: emptyString(),
        style = style,
        maxLines = 1,
        modifier = modifier
            .defaultMinSize(minWidth = Dp(style.fontSize.value * 12))
            .align(Alignment.Start)
            .padding(bottom = 2.dp)
            .fade(text == null),
    )
}

@Composable
internal fun EventFailure(message: String, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                modifier = modifier.padding(16.dp, 12.dp),
                text = message,
            )
        }
    }
}
