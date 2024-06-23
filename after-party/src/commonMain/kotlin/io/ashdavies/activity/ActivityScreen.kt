package io.ashdavies.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.android.fade
import io.ashdavies.events.Event
import io.ashdavies.paging.LazyPagingItems
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize

@Parcelize
internal object ActivityScreen : Parcelable, Screen {
    data class State(val pagingItems: LazyPagingItems<Event>) : CircuitUiState
}

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
internal fun ActivityScreen(state: ActivityScreen.State, modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { ActivityTopAppBar("Events") },
    ) { contentPadding ->
        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.pagingItems.loadState.isRefreshing,
            onRefresh = { state.pagingItems.refresh() },
        )

        Box(
            modifier = Modifier
                .padding(contentPadding)
                .pullRefresh(pullRefreshState),
        ) {
            if (state.pagingItems.loadState.hasError) {
                EventFailure(state.pagingItems.loadState.errorMessage ?: "Unknown Error")
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = state.pagingItems.loadState.isRefreshing,
                state = pullRefreshState,
            )

            LazyColumn(Modifier.fillMaxSize()) {
                items(state.pagingItems.itemCount) {
                    EventSection(state.pagingItems[it])
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
private fun EventSection(event: Event?) {
    Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Button(
            onClick = { },
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
                        text = event?.name,
                        modifier = Modifier.align(Alignment.Start),
                        style = MaterialTheme.typography.labelLarge,
                    )

                    PlaceholderText(
                        text = event?.location,
                        modifier = Modifier.align(Alignment.Start),
                        style = MaterialTheme.typography.labelMedium,
                    )

                    PlaceholderText(
                        text = event?.dateStart,
                        modifier = Modifier.align(Alignment.Start),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
internal fun PlaceholderText(
    text: String?,
    modifier: Modifier = Modifier,
    verticalPadding: Dp = 2.dp,
    characters: Int = 12,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        overflow = TextOverflow.Ellipsis,
        text = text ?: String(),
        style = style,
        maxLines = 1,
        modifier = modifier
            .padding(vertical = verticalPadding)
            .defaultMinSize(minWidth = Dp(style.fontSize.value * characters))
            .fade(text == null),
    )
}

@Composable
private fun EventFailure(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        modifier = modifier.padding(16.dp, 12.dp),
        color = MaterialTheme.colorScheme.error,
    )
}
