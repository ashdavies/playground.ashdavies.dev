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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.ashdavies.graphics.rememberAsyncImagePainter

@Composable
@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
public fun LauncherScreen(
    state: LauncherScreen.State,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState()
    val eventSink = state.eventSink

    Scaffold(
        topBar = { LauncherTopAppBar() },
        modifier = modifier,
    ) { contentPadding ->
        HorizontalPager(
            pageCount = state.entries.size,
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
                    model = entry.image,
                    contentScale = ContentScale.FillHeight,
                ),
                contentDescription = entry.title,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background),
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
