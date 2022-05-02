package io.ashdavies.playground.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import io.ashdavies.playground.EventsRoot
import io.ashdavies.playground.TopAppBar
import io.ashdavies.playground.android.FlowRow
import io.ashdavies.playground.android.fade
import io.ashdavies.playground.compose.EmptyPainter
import io.ashdavies.playground.compose.rememberState
import io.ashdavies.playground.network.OpenUri
import io.ashdavies.playground.platform.PlaygroundBottomBar
import io.ashdavies.playground.profile.ProfileViewState.LogIn
import io.ashdavies.playground.profile.ProfileViewState.LoggedIn
import io.ashdavies.playground.profile.ProfileViewState.LoggedOut
import io.ashdavies.playground.rememberInsetsPaddingValues
import io.kamel.core.Resource
import io.kamel.core.getOrElse
import io.kamel.core.isLoading
import io.kamel.image.lazyPainterResource
import kotlin.random.Random.Default.nextInt

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ProfileScreen(child: EventsRoot.Child.Profile) {
    val viewModel: ProfileViewModel = rememberProfileViewModel()
    val viewState: ProfileViewState by rememberState(viewModel.viewState)

    Scaffold(
        topBar = { ProfileTopAppBar() },
        bottomBar = { PlaygroundBottomBar(child) }
    ) { contentPadding ->
        ProfileScreen(
            modifier = Modifier.padding(contentPadding),
            onLogin = { viewModel.onLogin() },
            viewState = viewState,
        )
    }
}

@Composable
private fun ProfileTopAppBar() {
    TopAppBar(
        contentPadding = rememberInsetsPaddingValues(applyBottom = false),
        title = { Text("Profile") }
    )
}

@Composable
private fun ProfileScreen(viewState: ProfileViewState, modifier: Modifier = Modifier, onLogin: () -> Unit = { }) {
    val resourcePainter: Resource<Painter> = (viewState as? LoggedIn)?.picture
        ?.let { lazyPainterResource(it) }
        ?: Resource.Success(EmptyPainter)

    Box(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            when (viewState) {
                is LogIn -> OpenUri(viewState.uriString)
                is LoggedOut -> LoggedOutScreen(onLogin)
                else -> {
                    ProfileHeader(resourcePainter, viewState as LoggedIn)
                    StubLanyardFlow()
                }
            }
        }

    }
}

@Composable
private fun ProfileHeader(resourcePainter: Resource<Painter>, viewState: LoggedIn) {
    Card(
        modifier = Modifier
            .padding(top = 64.dp, bottom = 12.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .padding(4.dp, 64.dp, 4.dp, 4.dp)
                    .fade(resourcePainter.isLoading),
                style = MaterialTheme.typography.headlineSmall,
                text = viewState.name
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
private fun LoggedInFooter(resourcePainter: Resource<Painter>) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Image(
            painter = resourcePainter.getOrElse({ EmptyPainter }, { EmptyPainter }),
            contentDescription = null,
            modifier = Modifier
                .fade(resourcePainter.isLoading)
                .align(CenterHorizontally)
                .border(2.dp, Color.LightGray, CircleShape)
                .clip(CircleShape)
                .size(128.dp)
        )
    }
}
