package io.ashdavies.playground

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.analytics.OnClick
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val LauncherAspectRatio: Float
    get() = 1024f / 500f

@Parcelize
internal object LauncherScreen : Parcelable, Screen {

    data class State(
        val entries: List<Item>,
        val eventSink: (NavEvent) -> Unit,
    ) : CircuitUiState {

        interface Item {
            val imageModel: Any
            val screen: Screen
            val title: String
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun LauncherScreen(
    state: LauncherScreen.State,
    modifier: Modifier = Modifier,
    reportFullyDrawn: () -> Unit,
) {
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        topBar = { LauncherTopAppBar() },
    ) { contentPadding ->
        LazyColumn(Modifier.padding(contentPadding)) {
            items(state.entries) { entry ->
                LauncherItem(
                    item = entry,
                    onClick = OnClick("launcher_goto", mapOf("screen" to entry.screen)) {
                        eventSink(NavEvent.GoTo(entry.screen))
                    },
                    modifier = Modifier.padding(24.dp),
                )
            }
        }
    }

    val latestReportFullyDrawn by rememberUpdatedState(reportFullyDrawn)

    LaunchedEffect(Unit) {
        latestReportFullyDrawn()
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                )
            }
        },
    )
}

@Composable
@ExperimentalMaterial3Api
private fun LauncherItem(
    item: LauncherScreen.State.Item,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.clickable(
            onClickLabel = item.title,
            onClick = onClick,
        ),
    ) {
        Column {
            val imagePainter = when (val imageModel = item.imageModel) {
                is DrawableResource -> painterResource(imageModel)

                else -> rememberAsyncImagePainter(
                    model = item.imageModel,
                    contentScale = ContentScale.Crop,
                )
            }

            Image(
                painter = imagePainter,
                contentDescription = item.title,
                modifier = Modifier
                    .aspectRatio(LauncherAspectRatio)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )

            Row {
                Image(
                    painter = imagePainter,
                    contentDescription = item.title,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                )

                Text(
                    text = item.title,
                    modifier = Modifier.padding(
                        horizontal = 4.dp,
                        vertical = 14.dp,
                    ),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}
