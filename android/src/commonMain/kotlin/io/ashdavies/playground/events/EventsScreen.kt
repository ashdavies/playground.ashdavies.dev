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
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.ashdavies.playground.Event
import io.ashdavies.playground.LocalPlaygroundDatabase
import io.ashdavies.playground.platform.PlatformScaffold
import io.ashdavies.playground.platform.PlatformSwipeRefresh
import io.ashdavies.playground.platform.PlatformTopAppBar
import io.ashdavies.playground.platform.PlaygroundBottomBar
import io.ashdavies.playground.PlaygroundRoot
import io.ashdavies.playground.android.LazyPagingItems
import io.ashdavies.playground.android.collectAsLazyPagingItems
import io.ashdavies.playground.android.errorMessage
import io.ashdavies.playground.android.fade
import io.ashdavies.playground.android.isRefreshing
import io.ashdavies.playground.android.items
import io.ashdavies.playground.android.refresh
import io.ashdavies.playground.android.viewModel
import io.ashdavies.playground.network.LocalHttpClient
import io.ktor.client.HttpClient

@Composable
internal fun EventsScreen(child: PlaygroundRoot.Child.Events) {
    val httpClient: HttpClient = LocalHttpClient.current
    val eventsService: EventsService = remember {
        EventsService(httpClient)
    }

    val playgroundDatabase = LocalPlaygroundDatabase.current
    val eventsQueries = playgroundDatabase.eventsQueries

    val viewModel: EventsViewModel = viewModel {
        EventsViewModel(eventsQueries, eventsService)
    }

    val pagingItems: LazyPagingItems<Event> = viewModel
        .pagingData
        .collectAsLazyPagingItems()

    PlatformScaffold(
        topBar = { PlatformTopAppBar("Events") },
        bottomBar = { PlaygroundBottomBar(child) }
    ) { contentPadding ->
        PlatformSwipeRefresh(
            isRefreshing = pagingItems.isRefreshing,
            onRefresh = pagingItems::refresh,
        ) {
            if (pagingItems.errorMessage != null) {
                EventFailure(pagingItems.errorMessage)
            }

            LazyColumn(
                contentPadding = contentPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp),
            ) {
                items(pagingItems) {
                    EventSection(it) {
                        println("Clicked ${it?.name}")
                    }
                }
            }
        }
    }
}

@Composable
private fun EventSection(event: Event?, onClick: () -> Unit) {
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
                    PlaceholderText(event?.name, style = MaterialTheme.typography.h6)
                    PlaceholderText(event?.location, style = MaterialTheme.typography.body1)
                    PlaceholderText(event?.dateStart, style = MaterialTheme.typography.body2)
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
            .padding(bottom = 2.dp)
            .defaultMinSize(minWidth = Dp(style.fontSize.value * 12))
            .align(Alignment.Start)
            .fade(text == null),
    )
}

@Composable
private fun EventFailure(message: String?, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                modifier = modifier.padding(16.dp, 12.dp),
                text = message ?: "Something went wrong",
            )
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
private inline fun emptyString(): String = ""
