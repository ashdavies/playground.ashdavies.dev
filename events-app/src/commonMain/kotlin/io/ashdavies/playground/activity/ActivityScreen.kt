package io.ashdavies.playground.activity

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.items
import io.ashdavies.playground.Event
import io.ashdavies.playground.android.fade
import io.ashdavies.playground.platform.PlatformSwipeRefresh

private val <T : Any> LazyPagingItems<T>.errorMessage: String?
    get() = (loadState.append as? LoadStateError)
        ?.error
        ?.message

private val <T : Any> LazyPagingItems<T>.isRefreshing: Boolean
    get() = loadState.refresh is LoadStateLoading

@Composable
@ExperimentalMaterial3Api
internal fun ActivityScreen(state: ActivityScreen.State, modifier: Modifier = Modifier) {
    val eventSink = state.eventSink

    Scaffold(
        topBar = { ActivityTopAppBar("Events") },
        modifier = modifier,
    ) { contentPadding ->
        PlatformSwipeRefresh(
            isRefreshing = state.pagingItems.isRefreshing,
            onRefresh = state.pagingItems::refresh,
        ) {
            val errorMessage = state.pagingItems.errorMessage
            if (errorMessage != null) {
                EventFailure(errorMessage)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
            ) {
                items(state.pagingItems) {
                    EventSection(it) { event ->
                        eventSink(ActivityScreen.Event.Details(event.id))
                    }
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ActivityTopAppBar(text: String = "Events", modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Row {
                Text(text = text)

                Icon(
                    painter = rememberVectorPainter(Icons.Filled.ArrowDropDown),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null,
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun EventSection(event: Event?, onClick: (Event) -> Unit) {
    Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Button(
            onClick = { if (event != null) onClick(event) },
            modifier = Modifier.fillMaxWidth(),
            enabled = event != null,
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight(),
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp),
                ) {
                    PlaceholderText(
                        style = MaterialTheme.typography.labelLarge,
                        text = event?.name,
                    )

                    PlaceholderText(
                        style = MaterialTheme.typography.labelMedium,
                        text = event?.location,
                    )

                    PlaceholderText(
                        style = MaterialTheme.typography.labelSmall,
                        text = event?.dateStart,
                    )
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
        text = text ?: String(),
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
private fun EventFailure(message: String, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                modifier = modifier.padding(16.dp, 12.dp),
                text = message,
            )
        }
    }
}
