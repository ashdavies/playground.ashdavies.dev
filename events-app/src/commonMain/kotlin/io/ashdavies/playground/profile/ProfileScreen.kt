package io.ashdavies.playground.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import io.ashdavies.http.isLoading
import io.ashdavies.playground.EmptyPainter
import io.ashdavies.playground.android.FlowRow
import io.ashdavies.playground.android.fade
import io.ashdavies.playground.produceImagePainterState
import kotlin.random.Random.Default.nextInt

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ProfileScreen(state: ProfileScreen.State, modifier: Modifier = Modifier) {
    val eventSink = state.eventSink

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) },
        modifier = modifier,
    ) { contentPadding ->
        ProfileScreen(
            onLogin = { eventSink(ProfileScreen.Event.Login) },
            modifier = Modifier.padding(contentPadding),
            state = state,
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ProfileScreen(
    state: ProfileScreen.State,
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
) {
    Box(modifier) {
        Column(Modifier.padding(16.dp)) {
            when (state.profile) {
                null -> LoggedOutScreen(onLogin)
                else -> {
                    val painter by produceImagePainterState(state.profile.picture)
                    ProfileHeader(state.profile.name, painter)
                    StubLanyardFlow()
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ProfileHeader(
    text: String,
    painter: Result<Painter>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(top = 64.dp, bottom = 12.dp)
            .fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = text,
                modifier = Modifier
                    .padding(4.dp, 64.dp, 4.dp, 4.dp)
                    .fade(painter.isLoading),
            )
        }
    }

    Text(
        modifier = Modifier.padding(bottom = 4.dp),
        style = MaterialTheme.typography.headlineSmall,
        text = "Events",
    )
}

@Composable
private fun StubLanyardFlow(until: Int = 30) {
    FlowRow {
        repeat(nextInt(until)) {
            Image(
                painter = EmptyPainter,
                contentDescription = null,
                modifier = Modifier
                    .border(1.dp, Color.LightGray, CircleShape)
                    .size(64.dp, 64.dp)
                    .clip(CircleShape)
                    .fade(true)
                    .padding(4.dp),
            )
        }
    }
}

@Composable
private fun LoggedOutScreen(onLogin: () -> Unit) {
    Column {
        Text("Not logged in")

        Button(onClick = onLogin) {
            Text("Login")
        }
    }
}

@Composable
private fun LoggedInFooter(painter: Result<Painter>) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Image(
            painter = painter.getOrElse { EmptyPainter },
            contentDescription = null,
            modifier = Modifier
                .fade(painter.isLoading)
                .align(CenterHorizontally)
                .border(2.dp, Color.LightGray, CircleShape)
                .clip(CircleShape)
                .size(128.dp),
        )
    }
}
