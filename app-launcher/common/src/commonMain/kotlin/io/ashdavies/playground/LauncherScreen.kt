package io.ashdavies.playground

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

@Parcelize
internal object LauncherScreen : Parcelable, Screen {
    data class Entry(
        val imageModel: Any,
        val title: String,
        val event: Event,
    )

    sealed interface Event : CircuitUiEvent {
        data object AfterParty : Event
        data object Dominion : Event
        data object Routes : Event
    }

    data class State(
        val entries: List<Entry> = emptyList(),
        val eventSink: (Event) -> Unit = { },
    ) : CircuitUiState
}

@Composable
@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
internal fun LauncherScreen(
    state: LauncherScreen.State,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val pagerState = rememberPagerState { state.entries.size }
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { LauncherTopAppBar() },
    ) { contentPadding ->
        HorizontalPager(
            state = pagerState,
            contentPadding = contentPadding,
        ) { index ->
            val entry = state.entries[index]
            LauncherItem(
                entry = entry,
                modifier = modifier.padding(24.dp),
                onClick = { eventSink(entry.event) },
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun LauncherTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text("Launcher") },
        modifier = modifier,
        navigationIcon = {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                )
            }
        },
    )
}

@Composable
@ExperimentalMaterial3Api
private fun LauncherItem(
    entry: LauncherScreen.Entry,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(modifier = modifier.fillMaxHeight()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
                .width(IntrinsicSize.Max),
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = entry.imageModel,
                    contentScale = ContentScale.Crop,
                ),
                contentDescription = entry.title,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background),
                contentScale = ContentScale.Crop,
            )

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "Launch",
                )
            }
        }
    }
}
