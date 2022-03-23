package io.ashdavies.playground.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import io.ashdavies.playground.platform.PlatformScaffold
import io.ashdavies.playground.platform.PlatformTopAppBar
import io.ashdavies.playground.platform.PlaygroundBottomBar
import io.ashdavies.playground.PlaygroundRoot
import io.ashdavies.playground.android.FlowRow
import io.ashdavies.playground.android.fade
import io.ashdavies.playground.compose.EmptyPainter
import io.ashdavies.playground.network.LocalHttpClient
import io.ashdavies.playground.profile.ProfileViewState.LoggedIn
import io.ashdavies.playground.profile.ProfileViewState.LoggedOut
import io.kamel.core.Resource
import io.kamel.core.getOrElse
import io.kamel.core.isLoading
import io.kamel.image.lazyPainterResource
import kotlin.random.Random.Default.nextInt

private const val IDENTITY_ENDPOINT = "https://identitytoolkit.googleapis.com/v1/accounts"

@Composable
internal fun ProfileScreen(child: PlaygroundRoot.Child.Profile) {
    val profileService = profileService(LocalHttpClient.current, IDENTITY_ENDPOINT, "")
    val viewModel = ProfileViewModel(profileService)
    val viewState: ProfileViewState? by viewModel
        .viewState
        .collectAsState()

    val resourcePainter: Resource<Painter> = (viewState as? LoggedIn)?.picture
        ?.let { lazyPainterResource(it) }
        ?: Resource.Success(EmptyPainter)

    PlatformScaffold(
        topBar = { PlatformTopAppBar("Profile") },
        bottomBar = { PlaygroundBottomBar(child) }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (viewState is LoggedOut) {
                    Column {
                        Text("Not logged in")

                        Button(onClick = { viewModel.onLogin() }) {
                            Text("Login")
                        }
                    }

                    return@PlatformScaffold
                }

                (viewState as? LoggedIn)?.also { viewState ->
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
                                style = MaterialTheme.typography.h4,
                                text = viewState.name
                            )
                        }
                    }

                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        style = MaterialTheme.typography.h4,
                        text = "Events",
                    )

                    FlowRow {
                        repeat(nextInt(30)) {
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
            }

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
    }
}
